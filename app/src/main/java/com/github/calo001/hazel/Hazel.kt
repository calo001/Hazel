package com.github.calo001.hazel

import android.app.Application
import com.github.calo001.hazel.providers.AppInitializers
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

        AppInitializers(this).init()
    }
}