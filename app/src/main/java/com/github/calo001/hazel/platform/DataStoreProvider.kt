package com.github.calo001.hazel.platform

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.github.calo001.hazel.config.ColorVariant
import com.github.calo001.hazel.ui.theme.Dictionaries
import kotlinx.coroutines.flow.map

private val COLOR_SCHEME = stringPreferencesKey("color_scheme")
private val DICTIONARY = stringPreferencesKey("Dictionary")

class DataStoreProvider(val context: Context) {
    private val settingsDataStore = context.dataStore

    suspend fun setColorScheme(color: ColorVariant) {
        settingsDataStore.edit { settings ->
            settings[COLOR_SCHEME] = when(color) {
                ColorVariant.Blue -> "blue"
                ColorVariant.Green -> "green"
            }
        }
    }

    suspend fun setDictionary(dictionary: Dictionaries) {
        settingsDataStore.edit { settings ->
            settings[DICTIONARY] = dictionary.name
        }
    }

    val colorScheme = settingsDataStore.data.map { preferences ->
        when(preferences[COLOR_SCHEME]) {
            "blue" -> ColorVariant.Blue
            "green" -> ColorVariant.Green
            else -> ColorVariant.Green
        }
    }

    val dictionary = settingsDataStore.data.map { preferences ->
        when(preferences[DICTIONARY]) {
            Dictionaries.Cambridge.name -> Dictionaries.Cambridge
            Dictionaries.Oxford.name -> Dictionaries.Oxford
            Dictionaries.WordReference.name -> Dictionaries.WordReference
            else -> Dictionaries.WordReference
        }
    }
}