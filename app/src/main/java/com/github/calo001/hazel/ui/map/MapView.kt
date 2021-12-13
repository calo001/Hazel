package com.github.calo001.hazel.ui.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.huawei.hms.maps.OnMapReadyCallback

@Composable
fun MapView(onMapReadyCallback: OnMapReadyCallback) {
    AndroidView(factory = { context ->
        com.huawei.hms.maps.MapView(context).apply {
            getMapAsync(onMapReadyCallback)
        }
    })
}