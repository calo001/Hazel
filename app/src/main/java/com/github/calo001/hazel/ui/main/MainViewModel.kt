package com.github.calo001.hazel.ui.main

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.calo001.hazel.huawei.*
import com.github.calo001.hazel.model.hazeldb.*
import com.github.calo001.hazel.model.unsplash.UnsplashResult
import com.github.calo001.hazel.network.UnsplashServiceProvider
import com.github.calo001.hazel.repository.GalleryRepository
import com.github.calo001.hazel.repository.NetworkResult
import com.github.calo001.hazel.repository.UnsplashRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.lang.NullPointerException

class MainViewModel : ViewModel() {
    private val _hazelContent = MutableStateFlow<HazelContentStatus>(HazelContentStatus.Loading)
    val hazelContent = _hazelContent.asStateFlow()

    private val _weatherStatus = MutableStateFlow<WeatherStatus>(WeatherStatus.Loading)
    val weatherStatus = _weatherStatus.asStateFlow()

    private val _speechStatus = MutableStateFlow<SpeechStatus>(SpeechStatus.NoSpeech)
    val speechStatus = _speechStatus.asStateFlow()

    private val _dialogShareQRStatus = MutableStateFlow<DialogShareQRStatus>(DialogShareQRStatus.Normal)
    val dialogShareQRStatus = _dialogShareQRStatus.asStateFlow()

    fun updateDialogShareQRStatus(status: DialogShareQRStatus) {
        _dialogShareQRStatus.tryEmit(status)
    }

    fun updateWeatherStatus(status: WeatherStatus) {
        _weatherStatus.tryEmit(status)
    }

    fun updateSpeechStatus(status: SpeechStatus) {
        _speechStatus.tryEmit(status)
    }

    private val unsplashService = UnsplashServiceProvider.service
    private val unsplashRepository by lazy { UnsplashRepository(unsplashService) }
    private val searchHelper by lazy {
        (hazelContent.value as? HazelContentStatus.Success)?.content?.let {
            SearchHelper(it)
        }
    }

    private val _bitmapQR = MutableStateFlow<Bitmap?>(null)
    val bitmapQR = _bitmapQR.asStateFlow()

    private val networkHelper = NetworkHelper()
    private val galleryRepository = GalleryRepository(networkHelper)

    private val _textRecognitionStatus = MutableStateFlow<TextRecognitionStatus>(TextRecognitionStatus.Normal)
    val textRecognitionStatus = _textRecognitionStatus.asStateFlow()

    private val _barcodeStatus = MutableStateFlow<BarcodeDetectorStatus>(BarcodeDetectorStatus.Normal)
    val barcodeStatus = _barcodeStatus.asStateFlow()

    private val _galleryStatus = MutableStateFlow<GalleryStatus>(GalleryStatus.Loading)
    val galleryStatus: StateFlow<GalleryStatus> get() = _galleryStatus

    private val _searchStatus = MutableStateFlow<SearchStatus>(SearchStatus.Loading)
    val searchStatus: StateFlow<SearchStatus> get() = _searchStatus

    fun loadHazelContent(hazelDb: InputStream) {
        viewModelScope.launch(Dispatchers.IO) {
            val outputStream = ByteArrayOutputStream()

            val buf = ByteArray(1024)
            var len: Int
            try {
                while (hazelDb.read(buf).also { len = it } != -1) {
                    outputStream.write(buf, 0, len)
                }
                outputStream.close()
                hazelDb.close()

                val content = outputStream.toString()
                val data = Json.decodeFromString<HazelContent>(content)
                _hazelContent.tryEmit(HazelContentStatus.Success(data))
            } catch (e: IOException) {
                _hazelContent.tryEmit(HazelContentStatus.Error(e))
            }
        }
    }

    fun getUsefulExpressionCategoryId(categoryId: String): UsefulPhrase? {
        return (hazelContent.value as? HazelContentStatus.Success)
            ?.content
            ?.usefulPhrases
            ?.find { ufContent ->
                ufContent.id == categoryId
            }
    }

    fun getColors(): List<ColorHazel> {
        return (hazelContent.value as? HazelContentStatus.Success)
            ?.content
            ?.colorHazels
            ?: listOf()
    }

    fun getColorById(colorCode: String): ColorHazel? {
        return getColors().firstOrNull {
            it.id == colorCode
        }
    }

    fun getSeasonById(seasonId: String): Season? {
        return getSeasons().firstOrNull {
            it.id == seasonId
        }
    }

    fun getWeatherById(weatherId: String): Weather? {
        return getWeather().firstOrNull {
            it.id == weatherId
        }
    }

    fun getSeasons(): List<Season> {
        return (hazelContent.value as? HazelContentStatus.Success)
            ?.content
            ?.seasons
            ?: listOf()
    }

    fun searchUnsplash(query: String) = viewModelScope.launch {
        _galleryStatus.tryEmit(GalleryStatus.Loading)
        when (val result = unsplashRepository.search(query)) {
            is NetworkResult.Error -> _galleryStatus.tryEmit(GalleryStatus.Error(result.error))
            NetworkResult.Loading -> _galleryStatus.tryEmit(GalleryStatus.Loading)
            is NetworkResult.Success<*> -> {
                val unsplashResult = (result.content as? UnsplashResult)
                if (unsplashResult != null) {
                    _galleryStatus.tryEmit(GalleryStatus.Success(listOf()))
                } else {
                    _galleryStatus.tryEmit(GalleryStatus.Error(NullPointerException()))
                }
            }
        }
    }

    fun getToken(onTokenSuccess: (String) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        when(val result = galleryRepository.getToken()) {
            is NetworkResult.Error -> _galleryStatus.tryEmit(GalleryStatus.Error(result.error))
            NetworkResult.Loading -> _galleryStatus.tryEmit(GalleryStatus.Loading)
            is NetworkResult.Success<*> -> {
                val token = (result.content as? String)
                if (!token.isNullOrEmpty()) {
                    onTokenSuccess(token)
                } else {
                    _galleryStatus.tryEmit(GalleryStatus.Error(NullPointerException()))
                }
            }
        }
    }

    fun getAnimals(): List<Animal> {
        return (hazelContent.value as? HazelContentStatus.Success)
            ?.content
            ?.animals
            ?: listOf()
    }

    fun getCountries(): List<Country> {
        return (hazelContent.value as? HazelContentStatus.Success)
            ?.content
            ?.countries
            ?: listOf()
    }

    fun getIrregularVerbs(): List<Verb> {
        return (hazelContent.value as? HazelContentStatus.Success)
            ?.content
            ?.irregularVerbs
            ?: listOf()
    }

    fun getRegularVerbs(): List<Verb> {
        return (hazelContent.value as? HazelContentStatus.Success)
            ?.content
            ?.regularVerbs
            ?: listOf()
    }

    fun getWeather(): List<Weather> {
        return (hazelContent.value as? HazelContentStatus.Success)
            ?.content
            ?.weather
            ?: listOf()
    }

    fun searchQuery(query: List<String>) = viewModelScope.launch(Dispatchers.IO) {
        _searchStatus.tryEmit(SearchStatus.Loading)
        searchHelper?.searchQuery(query)?.apply {
            if (this.isNotEmpty()) {
                _searchStatus.tryEmit(SearchStatus.Success(this))
            } else {
                _searchStatus.tryEmit(SearchStatus.Error)
            }
        }
    }

    fun updateGalleryStatus(status: GalleryStatus) {
        _galleryStatus.tryEmit(status)
    }

    fun updateTextRecognitionStatus(status: TextRecognitionStatus) {
        _textRecognitionStatus.tryEmit(status)
    }

    fun updateBarcodeStatus(status: BarcodeDetectorStatus) {
        _barcodeStatus.tryEmit(status)
    }

    fun updateQrCode(bitmap: Bitmap) {
        _bitmapQR.tryEmit(bitmap)
    }
}

sealed interface HazelContentStatus {
    object Loading: HazelContentStatus
    class Success(val content: HazelContent): HazelContentStatus
    class Error(val error: Exception): HazelContentStatus
}

sealed interface GalleryStatus {
    object Loading: GalleryStatus
    class Success(val content: List<String>): GalleryStatus
    class Error(val error: Exception): GalleryStatus
}

sealed interface SearchStatus {
    object Loading: SearchStatus
    class Success(val result: List<SearchResult>): SearchStatus
    object Error: SearchStatus
}

sealed interface DialogShareQRStatus {
    object Normal: DialogShareQRStatus
    class RawRoute(val route: String): DialogShareQRStatus
    data class AppLinkingLink(val link: String, val route: String): DialogShareQRStatus
}