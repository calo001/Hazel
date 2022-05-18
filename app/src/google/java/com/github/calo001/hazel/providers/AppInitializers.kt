package com.github.calo001.hazel.providers

import android.content.Context
import com.huawei.hms.network.NetworkKit
import com.orhanobut.logger.Logger

class AppInitializers(private val context: Context) {
    fun init() {
        initNetworkKit()
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