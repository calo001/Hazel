package com.github.calo001.hazel.huawei

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.SpeechRecognizer
import com.huawei.hms.common.util.Logger
import com.huawei.hms.mlsdk.asr.MLAsrConstants
import com.huawei.hms.mlsdk.asr.MLAsrListener
import com.huawei.hms.mlsdk.asr.MLAsrRecognizer

class ASRHelper(val context: Context, val result: (SpeechStatus) -> Unit) {
    private val mSpeechRecognizer = MLAsrRecognizer.createAsrRecognizer(context)
    private val mSpeechRecognizerIntent = Intent(MLAsrConstants.ACTION_HMS_ASR_SPEECH)

    private fun setup() {
        mSpeechRecognizer?.setAsrListener(SpeechRecognitionListener())
        // Use Intent for recognition parameter settings.
        mSpeechRecognizerIntent
            // Set the language that can be recognized to English. If this parameter is not set, English is recognized by default. Example: "zh-CN": Chinese; "en-US": English; "fr-FR": French; "es-ES": Spanish; "de-DE": German; "it-IT": Italian; "ar": Arabic; "th_TH": Thai; "ms_MY": Malay; "fil_PH": Filipino.
            .putExtra(MLAsrConstants.LANGUAGE, MLAsrConstants.LAN_EN_US) // Set to return the recognition result along with the speech. If you ignore the setting, this mode is used by default. Options are as follows:
            // MLAsrConstants.FEATURE_WORDFLUX: Recognizes and returns texts through onRecognizingResults.
            // MLAsrConstants.FEATURE_ALLINONE: After the recognition is complete, texts are returned through onResults.
            .putExtra(MLAsrConstants.FEATURE, MLAsrConstants.FEATURE_ALLINONE) // Set the application scenario. MLAsrConstants.SCENES_SHOPPING indicates shopping, which is supported only for Chinese. Under this scenario, recognition for the name of Huawei products has been optimized.
            //.putExtra(MLAsrConstants.SCENES, MLAsrConstants.SCENES_SHOPPING)
    }

    fun startRecognizing() {
        setup()
        mSpeechRecognizer?.startRecognizing(mSpeechRecognizerIntent)
        result(SpeechStatus.Recording)
    }

    fun release() {
        mSpeechRecognizer?.destroy()
        result(SpeechStatus.NoSpeech)
    }

    internal inner class SpeechRecognitionListener : MLAsrListener {
        override fun onStartListening() {
            // The recorder starts to receive speech.
            Logger.i("onStartListening", "")
            result(SpeechStatus.Recording)
        }

        override fun onStartingOfSpeech() {
            // The user starts to speak, that is, the speech recognizer detects that the user starts to speak.
            Logger.i("onStartListening", "onStartingOfSpeech")
            result(SpeechStatus.Recording)
        }

        override fun onVoiceDataReceived(data: ByteArray, energy: Float, bundle: Bundle) {
            // Return the original PCM stream and audio power to the user. This API is not running in the main thread, and the return result is processed in the sub-thread.
            Logger.i("onVoiceDataReceived", "")
        }

        override fun onRecognizingResults(partialResults: Bundle) {
            // Receive the recognized text from MLAsrRecognizer. This API is not running in the main thread, and the return result is processed in the sub-thread.
            Logger.i("onRecognizingResults", "")
        }

        override fun onResults(results: Bundle) {
            Logger.i("onResults", "")
        }

        override fun onError(error: Int, errorMessage: String) {
            // Called when an error occurs in recognition. This API is not running in the main thread, and the return result is processed in the sub-thread.
            val error = when (error) {
                MLAsrConstants.ERR_NO_NETWORK -> "ERR_NO_NETWORK"
                MLAsrConstants.ERR_NO_UNDERSTAND -> "ERR_NO_UNDERSTAND"
                MLAsrConstants.ERR_SERVICE_UNAVAILABLE -> "ERR_SERVICE_UNAVAILABLE"
                else -> "unknown"
            }

            Logger.i("onError", error)
            result(SpeechStatus.NoSpeech)
        }

        override fun onState(state: Int, params: Bundle) {
            // Notify the app status change. This API is not running in the main thread, and the return result is processed in the sub-thread.
            val state = when (state) {
                MLAsrConstants.STATE_LISTENING -> "ERR_NO_NETWORK"
                MLAsrConstants.STATE_NO_SOUND -> "ERR_NO_UNDERSTAND"
                MLAsrConstants.STATE_NO_SOUND_TIMES_EXCEED -> "ERR_SERVICE_UNAVAILABLE"
                MLAsrConstants.STATE_NO_UNDERSTAND -> "STATE_NO_UNDERSTAND"
                MLAsrConstants.STATE_NO_NETWORK -> "STATE_NO_NETWORK"
                MLAsrConstants.STATE_WAITING -> "STATE_WAITING"
                else -> "unknown"
            }
            Logger.i("onState", state)
        }
    }
}

sealed interface SpeechStatus {
    object NoSpeech: SpeechStatus
    object Recording: SpeechStatus
    class Result(val text: String): SpeechStatus
}