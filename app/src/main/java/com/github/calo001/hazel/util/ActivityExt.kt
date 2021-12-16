package com.github.calo001.hazel.util

import android.app.Activity
import android.content.pm.PackageManager
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.ibm.icu.text.RuleBasedNumberFormat
import java.text.MessageFormat
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

fun Int.toWords(language: String = "en", country: String = "US"): String {
    val formatter = RuleBasedNumberFormat(
        Locale(language, country),
        RuleBasedNumberFormat.SPELLOUT
    )

    return formatter.format(this)
}