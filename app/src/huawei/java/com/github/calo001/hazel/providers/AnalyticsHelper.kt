package com.github.calo001.hazel.providers

import android.app.Activity
import com.huawei.hms.analytics.HiAnalytics
import com.huawei.hms.analytics.HiAnalyticsInstance
import com.huawei.hms.analytics.HiAnalyticsTools

class AnalyticsHelper(
    val activity: Activity
) {
    fun init() {
        HiAnalyticsTools.enableLog()
        val instance: HiAnalyticsInstance = HiAnalytics.getInstance(activity.applicationContext)
        instance.setAnalyticsEnabled(true)
    }
}