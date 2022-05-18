package com.github.calo001.hazel.providers

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import com.github.calo001.hazel.model.status.SpeechStatus
import com.github.calo001.hazel.platform.RecognizerContract

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
        recognizerResult.launch(0)
    }

    fun manageResponse(resultCode: Int, data: Intent?, dataString: String) {
        result(SpeechStatus.Result(dataString))
    }
}

fun ComponentActivity.getIntentSpeech(): Intent? {
    // Set the application scenario. MLAsrConstants.SCENES_SHOPPING indicates shopping, which is supported only for Chinese. Under this scenario, recognition for the name of Huawei products has been optimized.
    //.putExtra(MLAsrConstants.SCENES, MLAsrConstants.SCENES_SHOPPING)
    return null
}