package com.github.calo001.hazel.ui.ads

import android.content.Context
import com.huawei.hms.ads.HwAds

class AdInitializer(private val context: Context) {
    fun init() {
        HwAds.init(context)
    }
}