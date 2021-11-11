package com.github.calo001.hazel.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.github.calo001.hazel.config.ColorVariant
import com.github.calo001.hazel.ui.theme.HazelTheme
import com.github.calo001.hazel.R
import com.github.calo001.hazel.util.PainterIdentifier
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.ui.ExperimentalComposeUiApi
import com.github.calo001.hazel.util.browse
import java.util.*
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.isSystemInDarkTheme
import com.github.calo001.hazel.platform.DataStoreProvider
import com.github.calo001.hazel.ui.theme.Dictionaries
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    @ExperimentalComposeUiApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dataStore = DataStoreProvider(applicationContext)
        val hazelDb = resources.openRawResource(R.raw.hazel)
        viewModel.loadHazelContent(hazelDb)

        setContent {
            val colorScheme by dataStore.colorScheme.collectAsState(initial = ColorVariant.Green)
            val dictionary by dataStore.dictionary.collectAsState(initial = Dictionaries.Oxford)
            val systemUiController = rememberSystemUiController()
            val useDarkIcons = !isSystemInDarkTheme()

            HazelTheme(
                colorVariant = colorScheme
            ) {
                SystemBars(systemUiController, useDarkIcons)
                val scope = rememberCoroutineScope()
                val navController = rememberNavController()
                val painterIdentifier = PainterIdentifier(
                    resources = resources,
                    packageName = packageName,
                    default = R.drawable.ic_launcher_background
                )
                val hazelContentStatus by viewModel.hazelContent.collectAsState()

                Surface(
                    color = MaterialTheme.colors.background,
                ) {
                    Router(
                        navController = navController,
                        hazelContentStatus = hazelContentStatus,
                        painterIdentifier = painterIdentifier,
                        onListenClick = { speak(it) },
                        onOpenLink = { term -> openInBrowser(term, dictionary) },
                        onOpenMaps = { link -> openMaps(link) },
                        colorScheme = colorScheme,
                        dictionary = dictionary,
                        onSelectColorScheme = {
                            scope.launch { dataStore.setColorScheme(it) }
                        },
                        onSelectDictionary = {
                            scope.launch { dataStore.setDictionary(it) }
                        },
                        viewModel = viewModel,
                    )
                }
            }
        }
    }

    private fun openMaps(link: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")
        try {
            startActivity(intent)
        } catch (e: Exception) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
        }
    }

    private fun openInBrowser(term: String, dictionaries: Dictionaries) {
        val url = "${dictionaries.url}$term"
        browse(url)
    }

    private fun speak(text: String) {
        var tts: TextToSpeech? = null
        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result: Int? = tts?.setLanguage(Locale.US)
                if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED
                ) {
                    Log.e("TTS", "This Language is not supported")
                } else {
                    tts?.let {
                        speakOut(text, it)
                    }
                }
            } else {
                Log.e("TTS", "Initilization Failed!")
            }
        }
    }

    private fun speakOut(text: String, textToSpeech: TextToSpeech) {
        val utteranceId = this.hashCode().toString() + ""
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
    }

    @Composable
    private fun SystemBars(
        systemUiController: SystemUiController,
        useDarkIcons: Boolean
    ) {
        val color = MaterialTheme.colors.background
        SideEffect {
            systemUiController.setSystemBarsColor(
                color = color,
                darkIcons = useDarkIcons
            )

            systemUiController.setStatusBarColor(
                color = color,
                darkIcons = useDarkIcons
            )

            systemUiController.setNavigationBarColor(
                color = color,
                darkIcons = useDarkIcons
            )
        }
    }
}