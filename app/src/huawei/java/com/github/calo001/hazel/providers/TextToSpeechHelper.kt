package com.github.calo001.hazel.providers

import android.content.Context
import android.os.Bundle
import android.util.Pair
import com.github.calo001.hazel.model.status.PlayingText
import com.github.calo001.hazel.model.status.TextToSpeechConfiguration
import com.github.calo001.hazel.model.status.TextToSpeechStatus
import com.huawei.hms.mlsdk.tts.*
import com.orhanobut.logger.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TextToSpeechHelper(
    private val context: Context
) {
    private var ttsConfig: MLTtsConfig? = null
    private var ttsEngine: MLTtsEngine? = null
    private var ttsCallback: MLTtsCallback? = null

    private var playingText = PlayingText(listOf(), 0)
    private val _textToSpeechStatus = MutableStateFlow<TextToSpeechStatus>(TextToSpeechStatus.NoPlaying)
    val textToSpeechStatus: StateFlow<TextToSpeechStatus> get() = _textToSpeechStatus

    private var configuration = TextToSpeechConfiguration(
        isDefaultSpeedValueExceeded = false,
        voiceSpeed = 1f,
        languageCode = MLTtsConstants.TTS_EN_US,
        personCode = MLTtsConstants.TTS_SPEAKER_FEMALE_EN,
        volume = 1f
    )

    init {
        configureEnginePreferences()
        initializeEngine()
        initializeCallback()
    }

    private fun configureEnginePreferences() {
        ttsConfig = MLTtsConfig()
            .setLanguage(configuration.languageCode)
            .setPerson(configuration.personCode)
            .setSpeed(configuration.voiceSpeed)
            .setVolume(configuration.volume)
    }

    private fun initializeEngine() {
        if (ttsEngine == null) {
            ttsEngine = MLTtsEngine(ttsConfig)
        }
    }

    private fun changeTTSConfiguration(
        configuration: TextToSpeechConfiguration
    ) {
        this.configuration = configuration
        val newConfig = MLTtsConfig()
            .setLanguage(configuration.languageCode)
            .setPerson(configuration.languageCode)
            .setSpeed(configuration.voiceSpeed)
            .setVolume(configuration.volume)

        changeEnginePreferences(newConfig)
    }

    private fun changeEnginePreferences(config: MLTtsConfig) = ttsEngine?.updateConfig(config)

    fun startSpeak(text: String) {
        Logger.i("startSpeak")
        _textToSpeechStatus.tryEmit(TextToSpeechStatus.Loading)
        val sentences = text.split("\n")
        val paragraphsBunch = getParagraphs(sentences)
        playingText = PlayingText(paragraphsBunch, 0)
        speakText(playingText.paragraphs[playingText.paragraphPlaying])
    }

    private fun speakText(text: String) {
        Logger.i(text)
        ttsEngine?.speak(text, MLTtsEngine.QUEUE_APPEND)
    }

    private fun getParagraphs(paragraphs: List<String>) =
        paragraphs
            .filterNot { it.isEmpty() }
            .flatMap { para ->
                val parts = mutableListOf("")
                var index = 0
                para.split(" ").forEach { word ->
                    if (word.length + parts[index].length < 500) {
                        parts[index] = parts[index] + " " + word
                    } else {
                        index++
                        parts.add(index, "")
                        parts[index] = word
                    }
                }
                parts
            }

    fun stopSpeak() {
        ttsEngine?.let {
            it.stop()
            playingText = PlayingText(listOf(), 0)
            _textToSpeechStatus.tryEmit(TextToSpeechStatus.Stopped)
        }
    }

    fun reset() {
        ttsEngine?.let {
            it.stop()
            playingText = PlayingText(listOf(), 0)
            _textToSpeechStatus.tryEmit(TextToSpeechStatus.NoPlaying)
        }
    }

    fun pauseSpeak() {
        Logger.i("T2T Paused")
        ttsEngine?.pause()
    }

    fun resumeSpeak() = ttsEngine?.resume()

    fun release() {
        ttsCallback = null
        ttsEngine?.setTtsCallback(null)
        ttsEngine?.shutdown()
        playingText = PlayingText(listOf(), 0)
    }

    private fun initializeCallback() {
        ttsCallback = object : MLTtsCallback {
            override fun onError(taskId: String?, error: MLTtsError?) {
                Logger.i(error?.errorMsg ?: taskId ?: "error")
                stopSpeak()
                _textToSpeechStatus.tryEmit(TextToSpeechStatus.Stopped)
            }

            override fun onWarn(taskId: String?, warn: MLTtsWarn?) {
                // Alarm handling without affecting service logic.
            }

            // Return the mapping between the currently played segment and text.
            // start: start position of the audio segment in the input text;
            // end (excluded): end position of the audio segment in the input text.
            override fun onRangeStart(taskId: String?, start: Int, end: Int) {

            }

            // taskId: ID of an audio synthesis task corresponding to the audio.
            // audioFragment: audio data.
            // offset: offset of the audio segment to be transmitted in the queue. One audio synthesis task corresponds to an audio synthesis queue.
            // range: text area where the audio segment to be transmitted is located; range.first (included): start position; range.second (excluded): end position.
            override fun onAudioAvailable(
                taskId: String?,
                audioFragment: MLTtsAudioFragment?,
                offset: Int,
                p3: Pair<Int, Int>?,
                bundle: Bundle?
            ) {
                Logger.i(audioFragment.toString())
            }

            // Orden en reproduccion normal
            // EVENT_SYNTHESIS_START
            // EVENT_PLAY_START
            // EVENT_SYNTHESIS_END
            // -- se reproduce
            // EVENT_SYNTHESIS_COMPLETE
            // EVENT_PLAY_STOP

            override fun onEvent(taskId: String?, eventId: Int, bundle: Bundle?) {
                when (eventId) {
                    MLTtsConstants.EVENT_PLAY_START -> {
                        Logger.i("EVENT_PLAY_START")
                        _textToSpeechStatus.tryEmit(TextToSpeechStatus.Playing)
                    }
                    MLTtsConstants.EVENT_PLAY_PAUSE -> {
                        Logger.i("EVENT_PLAY_PAUSE")
                        _textToSpeechStatus.tryEmit(TextToSpeechStatus.Paused)
                    }
                    MLTtsConstants.EVENT_PLAY_RESUME -> {
                        Logger.i("EVENT_PLAY_RESUME")
                        _textToSpeechStatus.tryEmit(TextToSpeechStatus.Playing)
                    }
                    MLTtsConstants.EVENT_PLAY_STOP -> {
                        Logger.i("EVENT_PLAY_STOP")
                        if (playingText.paragraphPlaying >= playingText.paragraphs.lastIndex) {
                            _textToSpeechStatus.tryEmit(TextToSpeechStatus.Stopped)
                        } else {
                            synthesisNext()
                        }
                    }
                    MLTtsConstants.EVENT_SYNTHESIS_START -> {
                        Logger.i("EVENT_SYNTHESIS_START")
                    }
                    MLTtsConstants.EVENT_SYNTHESIS_END -> {
                        Logger.i("EVENT_SYNTHESIS_END")
                    }
                    MLTtsConstants.EVENT_SYNTHESIS_COMPLETE -> {
                        Logger.i("EVENT_SYNTHESIS_COMPLETE")
                    }
                    else -> Logger.i("OTHER")
                }
            }
        }

        setCallbackToEngine()
    }

    private fun setCallbackToEngine() {
        ttsEngine?.setTtsCallback(ttsCallback)
    }

    private fun synthesisNext() {
        if (playingText.paragraphPlaying < playingText.paragraphs.lastIndex) {
            playingText = playingText.copy(
                paragraphPlaying = playingText.paragraphPlaying.plus(1)
            )
            speakText(playingText.paragraphs[playingText.paragraphPlaying])
        } else {
            _textToSpeechStatus.tryEmit(TextToSpeechStatus.Finished)
        }
    }
}