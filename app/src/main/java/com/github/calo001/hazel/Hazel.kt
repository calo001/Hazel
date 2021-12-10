package com.github.calo001.hazel

import android.app.Application
import com.huawei.hms.mlsdk.common.MLApplication
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

class Hazel: Application() {
    override fun onCreate() {
        super.onCreate()

        Logger.addLogAdapter(object : AndroidLogAdapter() {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })

        initMLConfig()
    }

    private fun initMLConfig() {
        MLApplication.initialize(this)
        MLApplication.getInstance().apiKey = BuildConfig.HMS_API_KEY
    }
}