package com.github.calo001.hazel.providers

import android.content.Context
import android.graphics.Bitmap
import com.github.calo001.hazel.model.status.BarcodeDetectorStatus
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage


class BarcodeDetectorHelper(
    val context: Context,
    val onResultBD: (BarcodeDetectorStatus) -> Unit,
) {
    fun analyze(bitmap: Bitmap) {
        onResultBD(BarcodeDetectorStatus.Processing)
        val image = InputImage.fromBitmap(bitmap, 0)
        val scanner = BarcodeScanning.getClient()
        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                barcodes.firstOrNull()?.let { barcode ->
                    barcode.displayValue?.let { value ->
                        onResultBD(BarcodeDetectorStatus.Result(value))
                    } ?: onResultBD(BarcodeDetectorStatus.Error)
                } ?: run {
                    onResultBD(BarcodeDetectorStatus.Error)
                }
            }
            .addOnFailureListener {
                onResultBD(BarcodeDetectorStatus.Error)
            }
    }
}