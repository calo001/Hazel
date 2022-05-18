package com.github.calo001.hazel.providers

import android.content.Context
import android.graphics.Bitmap
import com.github.calo001.hazel.model.status.TextRecognitionStatus
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class TextRecognitionHelper(
    val context: Context,
    val onResultTR: (TextRecognitionStatus) -> Unit
) {
    fun analyze(bitmap: Bitmap) {
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val image = InputImage.fromBitmap(bitmap, 0)
        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                if(visionText.text.isNotEmpty()) {
                    onResultTR(TextRecognitionStatus.Result(visionText.text))
                } else {
                    onResultTR(TextRecognitionStatus.Error)
                }
            }
            .addOnFailureListener { e ->
                onResultTR(TextRecognitionStatus.Error)
            }
    }
}