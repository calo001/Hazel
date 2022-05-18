package com.github.calo001.hazel.providers

import android.content.Context
import android.net.Uri
import android.view.MotionEvent
import android.view.View
import com.github.calo001.hazel.util.isZero
import com.huawei.hms.panorama.Panorama
import com.huawei.hms.panorama.PanoramaInterface

class PanoramaHelper(val context: Context) {
    val instance = Panorama.getInstance().getLocalInstance(context)

    fun validate(imageUri: Uri): Boolean {
        return instance.init().isZero and instance.setImage(
            imageUri,
            PanoramaInterface.IMAGE_TYPE_SPHERICAL
        ).isZero
    }

    fun getView(context: Context): View = instance.view

    fun updateTouchEvent(motionEvent: MotionEvent) {
        instance.updateTouchEvent(motionEvent)
    }

    fun init() {
        instance.init()
    }

    fun deInit() {
        instance.deInit()
    }
}