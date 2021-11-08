package com.github.calo001.hazel.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.calo001.hazel.model.HazelContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

class MainViewModel : ViewModel() {
    private val _hazelContent = MutableStateFlow<HazelContentStatus>(HazelContentStatus.Loading)
    val hazelContent: StateFlow<HazelContentStatus> get() = _hazelContent

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
}

sealed interface HazelContentStatus {
    object Loading: HazelContentStatus
    class Success(val content: HazelContent): HazelContentStatus
    class Error(val error: Exception): HazelContentStatus
}