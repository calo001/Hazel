package com.github.calo001.hazel.providers

import android.content.Context
import android.graphics.Bitmap
import com.github.calo001.hazel.model.status.BarcodeDetectorStatus
import com.huawei.hiai.vision.barcode.BarcodeDetector
import com.huawei.hiai.vision.common.VisionCallback
import com.huawei.hiai.vision.common.VisionImage
import com.huawei.hiai.vision.visionkit.barcode.Barcode
import com.huawei.hiai.vision.visionkit.barcode.ZxingBarcodeConfiguration
import com.huawei.hiai.vision.visionkit.text.config.VisionTextConfiguration

class BarcodeDetectorHelper(
    val context: Context,
    val onResultBD: (BarcodeDetectorStatus) -> Unit,
) {
    fun analyze(bitmap: Bitmap) {
        val mBarcodeDetector = BarcodeDetector(context)
        val image = VisionImage.fromBitmap(bitmap)
        val config = ZxingBarcodeConfiguration.Builder()
            .setProcessMode(VisionTextConfiguration.MODE_IN)
            .build()
        mBarcodeDetector.configuration = config

        mBarcodeDetector.detect(image, null, object : VisionCallback<List<Barcode?>?> {
            override fun onResult(barcodes: List<Barcode?>?) {
                if (barcodes != null) {
                    val textBarCode = barcodes.map { barcode ->
                        if (barcode != null) {
                            barcode.text?.text ?: barcode.url?.url
                        } else {
                            null
                        }
                    }.firstOrNull()

                    if (textBarCode != null) {
                        onResultBD(BarcodeDetectorStatus.Result(textBarCode))
                    } else {
                        onResultBD(BarcodeDetectorStatus.Error)
                    }
                }
            }

            override fun onError(p0: Int) {
                onResultBD(BarcodeDetectorStatus.Error)
            }

            override fun onProcessing(v: Float) {
                onResultBD(BarcodeDetectorStatus.Processing)
            }

        })
    }
}