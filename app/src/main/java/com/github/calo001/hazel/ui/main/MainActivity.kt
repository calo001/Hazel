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
import com.github.calo001.hazel.huawei.LocationHelper
import com.github.calo001.hazel.platform.DataStoreProvider
import com.github.calo001.hazel.routes.Routes
import com.github.calo001.hazel.huawei.WeatherHelper
import com.github.calo001.hazel.huawei.WeatherStatus
import com.github.calo001.hazel.ui.common.SystemBars
import com.github.calo001.hazel.ui.settings.Dictionaries
import com.github.calo001.hazel.util.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    //private val weatherHelper by lazy { WeatherHelper(this) }
    //private val locationHelper by lazy { LocationHelper(this) }
    //private val locationHelperResult by lazy { LocationHelper(this) }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val weatherHelper = WeatherHelper(this) { status ->
            viewModel.updateWeatherStatus(status)
        }
        weatherHelper.checkWeatherFromResult(requestCode = requestCode)
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

        val dataStore = DataStoreProvider(applicationContext)
        val hazelDb = resources.openRawResource(R.raw.hazel)
        val locationHelper = LocationHelper(this)
        val weatherHelper = WeatherHelper(this) { status ->
            viewModel.updateWeatherStatus(status)
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

                Surface(
                    color = MaterialTheme.colors.background,
                ) {
                    Router(
                        viewModel = viewModel,
                        weatherStatus = weatherStatus,
                        navController = navController,
                        hazelContentStatus = hazelContentStatus,
                        painterIdentifier = painterIdentifier,
                        onListenClick = { speak(it) },
                        onOpenLink = { term -> openInBrowser(term, dictionary) },
                        onOpenMaps = { link -> openMaps(link) },
                        colorScheme = colorScheme,
                        dictionary = dictionary,
                        darkMode = darkMode,
                        defaultRoute = Routes.Main.name,
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
                        }
                    )
                }
            }
        }
    }
}