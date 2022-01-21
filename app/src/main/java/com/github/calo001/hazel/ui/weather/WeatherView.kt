package com.github.calo001.hazel.ui.weather

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.calo001.hazel.huawei.SpeechStatus
import com.github.calo001.hazel.model.hazeldb.Weather
import com.github.calo001.hazel.ui.ads.SimpleRoundedBanner
import com.github.calo001.hazel.ui.common.HazelToolbarContent
import com.github.calo001.hazel.ui.common.SurfaceToolbar
import com.github.calo001.hazel.ui.common.TextImageRow
import com.github.calo001.hazel.ui.common.safeSpacer
import com.github.calo001.hazel.util.PainterIdentifier

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun WeatherView(
    modifier: Modifier = Modifier,
    weathers: List<Weather>,
    speechStatus: SpeechStatus,
    onTextChangeSpeech: (String) -> Unit,
    onClickWeather: (Weather) -> Unit,
    painterIdentifier: PainterIdentifier,
    onBackClick: () -> Unit,
    onSpeechClick: () -> Unit,

    ) {
    var querySearch by rememberSaveable { mutableStateOf("") }
    if (speechStatus is SpeechStatus.Result) {
        val inputSpeech = speechStatus.text.trim()
        if (inputSpeech.isNotEmpty()) {
            onTextChangeSpeech(inputSpeech)
            querySearch = inputSpeech
        }
    }

    Box(
        modifier = modifier
            .background(MaterialTheme.colors.background)
            .fillMaxSize()
    ) {
        LazyColumn {
            safeSpacer(extraSpace = 100.dp)

            val items = if (querySearch.isNotEmpty()) {
                weathers.filter {
                    it.name.lowercase().contains(querySearch.lowercase())
                }
            } else {
                weathers
            }
            items(
                count = items.size,
                key = { index -> items[index].id }
            ) { index ->
                val painters = items[index].emojiCodes.map { painterIdentifier.getPainter(identifier = it) }
                TextImageRow(
                    text = items[index].name,
                    images = painters,
                    modifier = Modifier
                        .clickable { onClickWeather(items[index]) }
                        .padding(vertical = 18.dp)
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .animateItemPlacement()
                )
            }
            item {
                SimpleRoundedBanner(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp)
                        .height(80.dp)
                        .fillMaxWidth()
                )
            }
        }

        SurfaceToolbar {
            HazelToolbarContent(
                title = "Weather",
                subtitle = "Basic vocabulary",
                onBackClick = onBackClick,
                onTextChange = {
                    querySearch = it
                },
                speechStatus = speechStatus,
                onSpeechClick = onSpeechClick,
                onTextChangeSpeech = onTextChangeSpeech,
            )
        }
    }
}

@Preview
@Composable
fun WeatherViewPreview() {

}