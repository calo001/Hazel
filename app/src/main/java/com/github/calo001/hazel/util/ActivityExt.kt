package com.github.calo001.hazel.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.ibm.icu.text.RuleBasedNumberFormat
import java.util.*

fun Activity.checkPermission(permissionName: String): Boolean {
    return ActivityCompat.checkSelfPermission(this, permissionName) == PackageManager.PERMISSION_GRANTED
}

fun View.setAllMatchParent() {
    layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT,
    )
}

fun View.setWidthMatchParent() {
    layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT,
    )
}

fun Int.toWords(language: String = "en", country: String = "US"): String {
    val formatter = RuleBasedNumberFormat(
        Locale(language, country),
        RuleBasedNumberFormat.SPELLOUT
    )

    return formatter.format(this)
}

fun Context.shareUrl(url: String, title: String) {
    val share = Intent.createChooser(Intent().apply {
        action = Intent.ACTION_SEND
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, title)
        putExtra(Intent.EXTRA_TEXT, url)

        // (Optional) Here we're setting the title of the content
        putExtra(Intent.EXTRA_TITLE, title)

        // (Optional) Here we're passing a content URI to an image to be displayed
        //data = Uri.parse(url)
        //flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    }, title)
    startActivity(share)
}