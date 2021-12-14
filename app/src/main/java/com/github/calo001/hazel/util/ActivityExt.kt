package com.github.calo001.hazel.util

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.orhanobut.logger.Logger
import java.util.*

fun Activity.openMaps(link: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")
    try {
        startActivity(intent)
    } catch (e: Exception) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
    }
}

fun Activity.speak(text: String) {
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

fun Activity.speakOut(text: String, textToSpeech: TextToSpeech) {
    val utteranceId = this.hashCode().toString() + ""
    textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
}

fun Activity.checkPermission(permissionName: String): Boolean {
    return ActivityCompat.checkSelfPermission(this, permissionName) == PackageManager.PERMISSION_GRANTED
}

fun View.setAllMatchParent() {
    layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT,
    )
}