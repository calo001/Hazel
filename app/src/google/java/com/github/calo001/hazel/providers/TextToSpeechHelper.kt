package com.github.calo001.hazel.providers

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import com.github.calo001.hazel.model.status.TextToSpeechStatus
import com.orhanobut.logger.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*

class TextToSpeechHelper(
    private val context: Context
) {
    private val _textToSpeechStatus = MutableStateFlow<TextToSpeechStatus>(TextToSpeechStatus.NoPlaying)
    val textToSpeechStatus: StateFlow<TextToSpeechStatus> get() = _textToSpeechStatus

    private val textToSpeech = TextToSpeech(context) { status ->
        initializeCallbackAndroid(status)
        if (status == TextToSpeech.SUCCESS) {
            configureEnginePreferences()
        }
    }

    private fun initializeCallbackAndroid(status: Int) {
        Logger.i("callback: $status")
    }

    private fun configureEnginePreferences() {
        textToSpeech.language = Locale.US
    }

    private fun initializeEngine() {
    }

    fun startSpeak(text: String) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, Bundle(), null)
    }

    private fun speakText(text: String) {
    }

    private fun getParagraphs(paragraphs: List<String>) {}

    fun stopSpeak() {
    }

    fun reset() {
    }

    fun pauseSpeak() {
    }

    fun resumeSpeak() {
    }

    fun release() {
        textToSpeech.stop()
        textToSpeech.shutdown()
    }

    private fun initializeCallback() {
    }

    private fun setCallbackToEngine() {
    }

    private fun synthesisNext() {}
}