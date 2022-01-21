package com.github.calo001.hazel

import android.app.Application
import com.huawei.hiai.vision.common.ConnectionCallback
import com.huawei.hiai.vision.common.VisionBase
import com.huawei.hms.ads.HwAds
import com.huawei.hms.mlsdk.common.MLApplication
import com.huawei.hms.network.NetworkKit
import com.huawei.hms.searchkit.SearchKitInstance
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
        initNetworkKit()
        initSearchKit()
        initVision()
        initAds()
    }

    private fun initAds() {
        HwAds.init(this)
    }

    private fun initVision() {
        /** Initialize with the VisionBase static class and asynchronously get the connection of the service */
        /** Initialize with the VisionBase static class and asynchronously get the connection of the service  */
        VisionBase.init(this, object : ConnectionCallback {
            override fun onServiceConnect() {
            }
            override fun onServiceDisconnect() {
            }
        })
    }

    private fun initSearchKit() {
        SearchKitInstance.init(this, "105049969");
    }

    private fun initMLConfig() {
        MLApplication.initialize(this)
        MLApplication.getInstance().apiKey = BuildConfig.HMS_API_KEY
    }

    /**
     * Asynchronously load the NetworkKit object.
     */
    private fun initNetworkKit() {
        // Initialization is performed once only. When the initialization method is called again, initialization is not performed but the onResult method is called again.
        NetworkKit.init(
            this, object : NetworkKit.Callback() {
                override fun onResult(result: Boolean) {
                    if (result) {
                        Logger.i("Networkkit init success")
                    } else {
                        Logger.i("Networkkit init failed")
                    }
                }
            })
    }
}