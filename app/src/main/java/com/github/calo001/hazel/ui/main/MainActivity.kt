package com.github.calo001.hazel.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.github.calo001.hazel.R
import com.github.calo001.hazel.config.ColorVariant
import com.github.calo001.hazel.config.DarkMode
import com.github.calo001.hazel.providers.*
import com.github.calo001.hazel.model.hazeldb.Country
import com.github.calo001.hazel.model.hazeldb.Season
import com.github.calo001.hazel.model.status.BarcodeDetectorStatus
import com.github.calo001.hazel.model.status.SpeechStatus
import com.github.calo001.hazel.model.status.TextRecognitionStatus
import com.github.calo001.hazel.model.status.WeatherStatus
import com.github.calo001.hazel.platform.DataStoreProvider
import com.github.calo001.hazel.platform.RecognizerContract
import com.github.calo001.hazel.routes.Routes
import com.github.calo001.hazel.ui.camera.CameraFeature
import com.github.calo001.hazel.ui.common.SystemBars
import com.github.calo001.hazel.ui.dialog.ShareDialog
import com.github.calo001.hazel.ui.map.MapActivity
import com.github.calo001.hazel.ui.panorama.PanoramaActivity
import com.github.calo001.hazel.ui.settings.Dictionaries
import com.github.calo001.hazel.ui.theme.HazelTheme
import com.github.calo001.hazel.util.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    private val panorama by lazy { PanoramaHelper(this) }
    private val textToSpeechHelper by lazy { TextToSpeechHelper(this) }
    private val asrHelper by lazy { ASRHelper(this) {
            viewModel.updateSpeechStatus(it)
        }
    }

    override fun onDestroy() {
        panorama.deInit()
        textToSpeechHelper.release()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launchWhenCreated {
            AGConnectALHelper(this@MainActivity)
                .initialize { route ->
                    viewModel.loadAppLinkedRoute(route)
                }
        }
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
            asrHelper.startRecognizing(getIntentSpeech(), speechResult, recognizerResult)
        }
    }

    private val speechResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        asrHelper.manageResponse(result.resultCode, result.data, "")
    }

    private val recognizerResult = registerForActivityResult(RecognizerContract()) {
        val result = it?.toString() ?: ""
        asrHelper.manageResponse(0, null, result)
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

    @SuppressLint("SourceLockedOrientationActivity", "MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAnalytics()
        panorama.init()
        AdInitializer(this).init()

        val dataStore = DataStoreProvider(applicationContext)
        val hazelDb = resources.openRawResource(R.raw.hazel)
        val locationHelper = LocationHelper(this)
        val weatherHelper = WeatherHelper(this) { status ->
            viewModel.updateWeatherStatus(status)
        }
        val rewardHelper = RewardAdHelper(this, this)
        val searchKitHelper = SearchKitHelper(this)
        viewModel.loadHazelContent(hazelDb)
        viewModel.initWithSearchKitHelper(searchKitHelper)

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
            //val isColorsUnlocked by dataStore.colorsUnlocked.collectAsState(initial = false)

            val useDarkIcons = when(darkMode) {
                DarkMode.Dark -> false
                DarkMode.Light -> true
                DarkMode.FollowSystem -> !isSystemInDarkTheme()
            }
            val localClipboardManager = LocalClipboardManager.current
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
                val dialogShareQRStatus by viewModel.dialogShareQRStatus.collectAsState()
                val deepLinkingStatus by viewModel.deepLinkingStatus.collectAsState()

                if(deepLinkingStatus is DeepLinkingStatus.AppLinkingRoute) {
                    runCatching {
                        (deepLinkingStatus as? DeepLinkingStatus.AppLinkingRoute)?.route?.let { route ->
                            navController.navigate(route)
                            viewModel.clearAppLinking()
                        }
                    }
                }

                navController.addOnDestinationChangedListener { _, destination, _ ->
                    requestedOrientation = when(destination.route) {
                        Routes.Camera.name ->
                            ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
                        else ->
                            ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT
                    }
                }
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    ShareDialog(
                        dialogShareQRStatus = dialogShareQRStatus,
                        onDismissRequest = { viewModel.updateDialogShareQRStatus(DialogShareQRStatus.Normal) },
                        updateDialogShareQRStatus = { statusDialog ->
                            viewModel.updateDialogShareQRStatus(statusDialog)
                        },
                        onShareUrl = { link ->
                            shareUrl(link, "Share Hazel")
                        }
                    )

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
                        isColorsUnlocked = true,
                        defaultRoute = Routes.Main.name,
                        speechStatus = speechStatus,
                        textToSpeechStatus = textToSpeechStatus,
                        onClickUnlockColors = {
                            rewardHelper.loadRewardAd {
                                scope.launch {
                                    dataStore.setColorsUnlocked(true)
                                }
                            }
                        },
                        onSpeechClick = {
                            if (speechStatus is SpeechStatus.NoSpeech ||
                                speechStatus is SpeechStatus.Result) {
                                if (checkPermission(Manifest.permission.RECORD_AUDIO)) {
                                    asrHelper.startRecognizing(getIntentSpeech(), speechResult, recognizerResult)
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
                        showQRDialog = { route ->
                            viewModel.updateDialogShareQRStatus(DialogShareQRStatus.RawRoute(route))
                        },
                        panoramaInterface = panorama,
                        onCopy = { text ->
                            localClipboardManager.setText(AnnotatedString(text))
                            toast("Text copied!")
                        },
                        onPanoramaClick = { season ->
                            startPanoramaActivity(season)
                        }, onAnalysisCapture = { bitmap, cameraFeature ->
                            when (cameraFeature) {
                                CameraFeature.QRReader -> {
                                    val barcodeHelper = BarcodeDetectorHelper(
                                        context = this,
                                        onResultBD = { status ->
                                            viewModel.updateBarcodeStatus(status)
                                            if (status is BarcodeDetectorStatus.Result) {
                                                lifecycleScope.launch(Dispatchers.Main) {
                                                    if (status.value.contains("https://calo001.github.io/hazel-web/")) {
                                                        val route = status.value
                                                            .split("https://calo001.github.io/hazel-web/")
                                                            .getOrElse(1) { "" }
                                                        if (route.isNotEmpty()) {
                                                            kotlin.runCatching {
                                                                navController.navigate(
                                                                    route
                                                                )
                                                            }
                                                        }
                                                    } else {
                                                        navController.navigate("${Routes.Camera.name}/results/qr")
                                                    }
                                                    Logger.i("navigate qr")
                                                }
                                            }
                                        },
                                    )
                                    barcodeHelper.analyze(bitmap)
                                }
                                CameraFeature.TextRecognizer -> {
                                    val textRecognitionHelper = TextRecognitionHelper(
                                        context = this,
                                        onResultTR = { status ->
                                            viewModel.updateTextRecognitionStatus(status)
                                            if (status is TextRecognitionStatus.Result) {
                                                lifecycleScope.launch(Dispatchers.Main) {
                                                    navController.navigate("${Routes.Camera.name}/results/text")
                                                    Logger.i("navigate qr")
                                                }
                                            }
                                        }
                                    )
                                    textRecognitionHelper.analyze(bitmap)
                                }
                            }
                        })
                }
            }
        }
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun initAnalytics() {
        AnalyticsHelper(this).init()
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