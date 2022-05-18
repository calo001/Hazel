package com.github.calo001.hazel.providers

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ComponentActivity
import com.github.calo001.hazel.model.status.SpeechStatus
import com.huawei.hms.mlplugin.asr.MLAsrCaptureActivity
import com.huawei.hms.mlplugin.asr.MLAsrCaptureConstants

class ASRHelper(
    private val activity: ComponentActivity,
    val result: (SpeechStatus) -> Unit
) {
    fun startRecognizing(
        intent: Intent?,
        speechResult: ActivityResultLauncher<Intent>,
        recognizerResult: ActivityResultLauncher<Int>,
    ) {
        result(SpeechStatus.Recording)
        // REQUEST_CODE_ASR: request code between the current activity and speech pickup UI activity. You can use this code to obtain the processing result of the speech pickup UI.
        //activity.startActivityForResult(intent, REQUEST_CODE_ASR)
        speechResult.launch(intent)
    }

    fun manageResponse(resultCode: Int, data: Intent?, dataString: String) {
        when (resultCode) {
            MLAsrCaptureConstants.ASR_SUCCESS -> if (data != null) {
                val bundle = data.extras
                // Obtain the text information recognized from speech.
                if (bundle!!.containsKey(MLAsrCaptureConstants.ASR_RESULT)) {
                    val text = bundle.getString(MLAsrCaptureConstants.ASR_RESULT).toString()
                    // Process the recognized text information.
                    result(SpeechStatus.Result(text))
                }
            }
            MLAsrCaptureConstants.ASR_FAILURE -> { // Processing logic for recognition failure.
                if (data != null) {
                    val bundle = data.extras
                    // Check whether a result code is contained.
                    if (bundle!!.containsKey(MLAsrCaptureConstants.ASR_ERROR_CODE)) {
                        val errorCode = bundle.getInt(MLAsrCaptureConstants.ASR_ERROR_CODE)
                        // Perform troubleshooting based on the result code.
                    }
                    // Check whether error information is contained.
                    if (bundle.containsKey(MLAsrCaptureConstants.ASR_ERROR_MESSAGE)) {
                        val errorMsg = bundle.getString(MLAsrCaptureConstants.ASR_ERROR_MESSAGE)
                        // Perform troubleshooting based on the error information.
                    }
                    // Check whether a sub-result code is contained.
                    if (bundle.containsKey(MLAsrCaptureConstants.ASR_SUB_ERROR_CODE)) {
                        val subErrorCode = bundle.getInt(MLAsrCaptureConstants.ASR_SUB_ERROR_CODE)
                        // Process the sub-result code.
                    }
                }
                result(SpeechStatus.NoSpeech)
            }
        }
    }
}

fun ComponentActivity.getIntentSpeech(): Intent? {
    // Set the application scenario. MLAsrConstants.SCENES_SHOPPING indicates shopping, which is supported only for Chinese. Under this scenario, recognition for the name of Huawei products has been optimized.
    //.putExtra(MLAsrConstants.SCENES, MLAsrConstants.SCENES_SHOPPING)
    return Intent(this, MLAsrCaptureActivity::class.java)
        // Set the language that can be recognized to English. If this parameter is not set, English is recognized by default. Example: "zh-CN": Chinese; "en-US": English; "fr-FR": French; "es-ES": Spanish; "de-DE": German; "it-IT": Italian; "ar": Arabic; "ru-RU": Russian; "th_TH": Thai; "ms_MY": Malay; "fil_PH": Filipino.
        .putExtra(MLAsrCaptureConstants.LANGUAGE, "en-US")
        // Set whether to display the recognition result on the speech pickup UI. MLAsrCaptureConstants.FEATURE_ALLINONE: no; MLAsrCaptureConstants.FEATURE_WORDFLUX: yes.
        .putExtra(MLAsrCaptureConstants.FEATURE, MLAsrCaptureConstants.FEATURE_WORDFLUX)
}