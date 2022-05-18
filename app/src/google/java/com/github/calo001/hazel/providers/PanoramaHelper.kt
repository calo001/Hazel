package com.github.calo001.hazel.providers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.MotionEvent
import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import com.github.calo001.hazel.util.setAllMatchParent

class PanoramaHelper(val context: Context) {
    private var bitmapHelper: Bitmap? = null

    fun init() {
    }

    fun deInit() {
    }

    fun validate(uri: Uri): Boolean {
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val src:ImageDecoder.Source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(src)
        } else{
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }
        bitmapHelper = bitmap
        return bitmap != null
    }

    fun getView(context: Context) = ComposeView(context).apply {
        setAllMatchParent()
        setContent {
            bitmapHelper?.asImageBitmap()?.let {
                Image(
                    bitmap = it,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    fun updateTouchEvent(touchEvent: MotionEvent) {

    }
}