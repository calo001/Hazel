package com.github.calo001.hazel.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.github.calo001.hazel.config.ColorVariant
import com.github.calo001.hazel.ui.theme.HazelTheme
import com.github.calo001.hazel.R
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import com.github.calo001.hazel.config.DarkMode
import com.github.calo001.hazel.huawei.*
import com.github.calo001.hazel.model.hazeldb.Country
import com.github.calo001.hazel.model.hazeldb.Season
import com.github.calo001.hazel.platform.DataStoreProvider
import com.github.calo001.hazel.routes.Routes
import com.github.calo001.hazel.ui.common.SystemBars
import com.github.calo001.hazel.ui.map.MapActivity
import com.github.calo001.hazel.ui.panorama.PanoramaActivity
import com.github.calo001.hazel.ui.settings.Dictionaries
import com.github.calo001.hazel.util.*
import com.huawei.hms.analytics.HiAnalytics
import com.huawei.hms.panorama.Panorama
import kotlinx.coroutines.launch
import com.huawei.hms.analytics.HiAnalyticsInstance
import com.huawei.hms.analytics.HiAnalyticsTools
import com.huawei.hms.analytics.type.HALogConfig


@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    private val panorama by lazy { Panorama.getInstance().getLocalInstance(this) }
    private val textToSpeechHelper by lazy { TextToSpeechHelper() }

    override fun onDestroy() {
        panorama.deInit()
        textToSpeechHelper.release()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val weatherHelper = WeatherHelper(this) { status ->
            viewModel.updateWeatherStatus(status)
        }
        weatherHelper.checkWeatherFromResult(requestCode = requestCode)
    }

    private val requestPermissionRecordAudio = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { result ->
        if (result) {
            val asrHelper = ASRHelper(this) { status ->
                viewModel.updateSpeechStatus(status)
            }
            asrHelper.startRecognizing()
        }
    }

    private val speechResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val asrHelper = ASRHelper2(this) {
            viewModel.updateSpeechStatus(it)
        }
        asrHelper.manageResponse(result.resultCode, result.data)
    }

    private fun startMapActivity(country: Country) {
        MapActivity.launch(this, country)
    }

    @SuppressLint("MissingPermission")
    val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        val weatherHelper = WeatherHelper(this) { status ->
            viewModel.updateWeatherStatus(status)
        }
        if (results.all { it.value }) {
            if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) and
                checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
            ) {
                LocationHelper(this).checkLocationSettings(
                    onLocationAvailable = {
                        weatherHelper.initWhetherListener()
                    },
                    onFailureLocationAvailable = {
                        viewModel.updateWeatherStatus(WeatherStatus.LocationFailure)
                    },
                    onSearchingLocation = {
                        viewModel.updateWeatherStatus(WeatherStatus.Loading)
                    }
                )
            }
        } else {
            viewModel.updateWeatherStatus(WeatherStatus.LocationNotGranted)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAnalytics()
        panorama.init()

        val dataStore = DataStoreProvider(applicationContext)
        val hazelDb = resources.openRawResource(R.raw.hazel)
        val locationHelper = LocationHelper(this)
        val weatherHelper = WeatherHelper(this) { status ->
            viewModel.updateWeatherStatus(status)
        }
        val asrHelper = ASRHelper2(this) { status ->
            viewModel.updateSpeechStatus(status)
        }
        viewModel.loadHazelContent(hazelDb)

        locationHelper.checkLocationSettings(
            onLocationAvailable = {
                if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) and
                    checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                ) {
                    weatherHelper.initWhetherListener()
                } else {
                    viewModel.updateWeatherStatus(WeatherStatus.LocationFailure)
                }
            },
            onFailureLocationAvailable = {
                viewModel.updateWeatherStatus(WeatherStatus.LocationFailure)
            },
            onSearchingLocation = {
                viewModel.updateWeatherStatus(WeatherStatus.Loading)
            },
            tryToGetPermissions = false
        )

        setContent {
            val colorScheme by dataStore.colorScheme.collectAsState(initial = ColorVariant.Green)
            val dictionary by dataStore.dictionary.collectAsState(initial = Dictionaries.Oxford)
            val darkMode by dataStore.darkMode.collectAsState(initial = DarkMode.FollowSystem)
            val useDarkIcons = when(darkMode) {
                DarkMode.Dark -> false
                DarkMode.Light -> true
                DarkMode.FollowSystem -> !isSystemInDarkTheme()
            }
            val systemUiController = rememberSystemUiController()
            val scope = rememberCoroutineScope()

            HazelTheme(
                darkTheme = when(darkMode) {
                    DarkMode.FollowSystem -> isSystemInDarkTheme()
                    DarkMode.Dark -> true
                    DarkMode.Light -> false
                },
                colorVariant = colorScheme
            ) {
                SystemBars(systemUiController, useDarkIcons)
                val navController = rememberNavController()
                val painterIdentifier = PainterIdentifier(
                    resources = resources,
                    packageName = packageName,
                    default = R.drawable.ic_launcher_foreground
                )
                val hazelContentStatus by viewModel.hazelContent.collectAsState()
                val weatherStatus by viewModel.weatherStatus.collectAsState()
                val speechStatus by viewModel.speechStatus.collectAsState()
                val textToSpeechStatus by textToSpeechHelper.textToSpeechStatus.collectAsState()

                Surface(
                    color = MaterialTheme.colors.background,
                ) {
                    Router(
                        viewModel = viewModel,
                        weatherStatus = weatherStatus,
                        navController = navController,
                        hazelContentStatus = hazelContentStatus,
                        painterIdentifier = painterIdentifier,
                        onListenClick = { textToSpeechHelper.startSpeak(it) },
                        onOpenLink = { term -> openInBrowser(term, dictionary) },
                        onOpenMaps = { country ->
                            startMapActivity(country)
                        },
                        colorScheme = colorScheme,
                        dictionary = dictionary,
                        darkMode = darkMode,
                        defaultRoute = Routes.Main.name,
                        speechStatus = speechStatus,
                        textToSpeechStatus = textToSpeechStatus,
                        onSpeechClick = {
                            if (speechStatus is SpeechStatus.NoSpeech ||
                                speechStatus is SpeechStatus.Result) {
                                if (checkPermission(Manifest.permission.RECORD_AUDIO)) {
                                    asrHelper.startRecognizing(speechResult)
                                } else {
                                    requestPermissionRecordAudio.launch(Manifest.permission.RECORD_AUDIO)
                                }
                            }
                        },
                        onSelectColorScheme = {
                            scope.launch { dataStore.setColorScheme(it) }
                        },
                        onSelectDictionary = {
                            scope.launch { dataStore.setDictionary(it) }
                        },
                        onSelectDarkMode = {
                            scope.launch { dataStore.setDarkMode(it) }
                        },
                        onRequestWeather = {
                            requestPermissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        },
                        panoramaInterface = panorama,
                        onPanoramaClick = { season ->
                            startPanoramaActivity(season)
                        }
                    )
                }
            }
        }
    }

    private fun initAnalytics() {
        HiAnalyticsTools.enableLog()
        val instance: HiAnalyticsInstance = HiAnalytics.getInstance(this)
    }

    private fun startPanoramaActivity(season: Season) {
        PanoramaActivity.launch(this, when(season.id) {
            "seasons_1" -> R.drawable.winter_panorama
            "seasons_2" -> R.drawable.spring_panoramic
            "seasons_3" -> R.drawable.autumn_panoramic
            "seasons_4" -> R.drawable.summer_panoramic
            else -> -1
        }, season.name)
    }
}