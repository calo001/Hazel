package com.github.calo001.hazel.huawei

import android.content.Context
import android.graphics.Bitmap
import com.huawei.hiai.vision.common.VisionCallback
import com.huawei.hiai.vision.common.VisionImage
import com.huawei.hiai.vision.text.TextDetector
import com.huawei.hiai.vision.visionkit.text.Text
import com.huawei.hiai.vision.visionkit.text.TextDetectType
import com.huawei.hiai.vision.visionkit.text.config.TextConfiguration
import com.huawei.hiai.vision.visionkit.text.config.VisionTextConfiguration


class TextRecognitionHelper(
    val context: Context,
    val onResultTR: (TextRecognitionStatus) -> Unit
) {
    fun analyze(bitmap: Bitmap) {
        val mTextDetector = TextDetector(context)
        val image = VisionImage.fromBitmap(bitmap)
        val result = Text()
        val config: VisionTextConfiguration = VisionTextConfiguration.Builder()
            .setAppType(VisionTextConfiguration.APP_NORMAL)
            .setProcessMode(VisionTextConfiguration.MODE_CAMERA)
            .setDetectType(TextDetectType.TYPE_TEXT_DETECT_FOCUS_SHOOT_EF)
            .setLanguage(TextConfiguration.ENGLISH)
            .build()

        mTextDetector.setVisionConfiguration(config)
        try {
            mTextDetector.detect(image, result, object: VisionCallback<Text> {
                override fun onResult(text: Text?) {
                    if (text != null && text.value != null && text.value.isNotEmpty()) {
                        onResultTR(TextRecognitionStatus.Result(text.value))
                    } else {
                        onResultTR(TextRecognitionStatus.Error)
                    }
                }

                override fun onError(p0: Int) {
                    onResultTR(TextRecognitionStatus.Error)
                }

                override fun onProcessing(p0: Float) {
                    onResultTR(TextRecognitionStatus.Processing)
                }

            })
        } catch (e: Exception) {
            onResultTR(TextRecognitionStatus.Error)
        }
    }
}

sealed interface TextRecognitionStatus {
    object Normal: TextRecognitionStatus
    object Processing: TextRecognitionStatus
    class Result(val value: String): TextRecognitionStatus
    object Error: TextRecognitionStatus
}