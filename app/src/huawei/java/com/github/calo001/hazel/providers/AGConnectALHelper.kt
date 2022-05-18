package com.github.calo001.hazel.providers

import android.app.Activity
import com.huawei.agconnect.applinking.AGConnectAppLinking
import com.orhanobut.logger.Logger

class AGConnectALHelper(val activity: Activity) {
    fun initialize(
        loadRoute: (String) -> Unit
    ) {
        AGConnectAppLinking.getInstance().getAppLinking(activity).addOnSuccessListener { resolvedLinkData ->
            val host = resolvedLinkData.deepLink.host ?: ""
            val slug = resolvedLinkData.deepLink.path?.removePrefix("/")?.removeSuffix("/") ?: ""
            if (host.contains("calo001.github.io")) {
                slug.split("hazel-web").getOrNull(1)?.let { route ->
                    Logger.i(
                        "${resolvedLinkData.deepLink.host}\n" +
                                "${resolvedLinkData.deepLink.path}\n" +
                                route
                    )
                    runCatching {
                        loadRoute(route.removePrefix("/").removeSuffix("/"))
                    }
                }
            }
        }.addOnCompleteListener {
            Logger.e(it?.exception?.localizedMessage ?: "complete applinking")
        }.addOnFailureListener {
            Logger.e(it?.localizedMessage ?: "error applinking")
        }
    }
}