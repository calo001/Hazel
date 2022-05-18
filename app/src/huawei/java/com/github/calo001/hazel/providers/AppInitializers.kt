package com.github.calo001.hazel.providers

import android.content.Context
import com.github.calo001.hazel.BuildConfig
import com.huawei.hiai.vision.common.ConnectionCallback
import com.huawei.hiai.vision.common.VisionBase
import com.orhanobut.logger.Logger
import com.huawei.hms.mlsdk.common.MLApplication
import com.huawei.hms.network.NetworkKit
import com.huawei.hms.searchkit.SearchKitInstance

class AppInitializers(private val context: Context) {
    fun init() {
        initMLConfig()
        initNetworkKit()
        initSearchKit()
        initVision()
        initAds()
    }

    private fun initAds() {}

    private fun initVision() {
        /** Initialize with the VisionBase static class and asynchronously get the connection of the service */
        /** Initialize with the VisionBase static class and asynchronously get the connection of the service  */
        VisionBase.init(context, object : ConnectionCallback {
            override fun onServiceConnect() {
            }
            override fun onServiceDisconnect() {
            }
        })
    }

    private fun initSearchKit() {
        SearchKitInstance.init(context, "105049969");
    }

    private fun initMLConfig() {
        MLApplication.initialize(context)
        MLApplication.getInstance().apiKey = BuildConfig.HMS_API_KEY
    }

    /**
     * Asynchronously load the NetworkKit object.
     */
    private fun initNetworkKit() {
        // Initialization is performed once only. When the initialization method is called again, initialization is not performed but the onResult method is called again.
        NetworkKit.init(
            context, object : NetworkKit.Callback() {
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