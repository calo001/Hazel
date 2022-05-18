package com.github.calo001.hazel.model.status

import com.github.calo001.hazel.providers.WeatherType

data class PlayingText(val paragraphs: List<String>, val paragraphPlaying: Int)

sealed interface TextToSpeechStatus {
    object NoPlaying: TextToSpeechStatus
    object Loading: TextToSpeechStatus
    object Playing: TextToSpeechStatus
    object Paused: TextToSpeechStatus
    object Stopped: TextToSpeechStatus
    object Finished: TextToSpeechStatus
}

data class TextToSpeechConfiguration(
    val isDefaultSpeedValueExceeded: Boolean,
    val voiceSpeed: Float,
    val languageCode: String,
    val personCode: String,
    val volume: Float,
)

sealed interface WeatherStatus {
    object Loading: WeatherStatus
    class  Success(val temperature: String, val typeWeather: WeatherType): WeatherStatus
    object LocationNotGranted: WeatherStatus
    object LocationFailure: WeatherStatus
    object Error: WeatherStatus
    object NoAvailable: WeatherStatus
}

sealed interface SpeechStatus {
    object NoSpeech: SpeechStatus
    object Recording: SpeechStatus
    class Result(val text: String): SpeechStatus
}

sealed interface BarcodeDetectorStatus {
    object Normal: BarcodeDetectorStatus
    object Processing: BarcodeDetectorStatus
    class Result(val value: String): BarcodeDetectorStatus
    object Error: BarcodeDetectorStatus
}

sealed interface TextRecognitionStatus {
    object Normal: TextRecognitionStatus
    object Processing: TextRecognitionStatus
    class Result(val value: String): TextRecognitionStatus
    object Error: TextRecognitionStatus
}