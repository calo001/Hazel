package com.github.calo001.hazel.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import com.github.calo001.hazel.ui.settings.Dictionaries

fun Color.Companion.parse(colorString: String): Color =
    Color(color = android.graphics.Color.parseColor(colorString))

val Color.complementaryBW get() = if (this.luminance() > 0.4) Color.Black else Color.White

fun Context.openInBrowser(term: String, dictionaries: Dictionaries) {
    val url = "${dictionaries.url}$term"
    browse(url)
}

fun Context.browse(url: String, newTask: Boolean = false) {
    try {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        if (newTask) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()
    }
}

fun String?.isNullOrEmptyElse(value: String): String {
    return if (this == null) value else if (this.isEmpty()) value else this
}

fun Boolean.isTrue(run: () -> Unit) {
    if (this) run()
}