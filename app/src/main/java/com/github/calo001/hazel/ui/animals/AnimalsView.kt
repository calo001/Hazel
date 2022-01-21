package com.github.calo001.hazel.ui.animals

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.github.calo001.hazel.huawei.SpeechStatus
import com.github.calo001.hazel.model.hazeldb.Animal
import com.github.calo001.hazel.ui.ads.SimpleBanner
import com.github.calo001.hazel.ui.ads.SimpleRoundedBanner
import com.github.calo001.hazel.ui.common.HazelToolbarContent
import com.github.calo001.hazel.ui.common.SurfaceToolbar
import com.github.calo001.hazel.ui.common.TextImageRow
import com.github.calo001.hazel.ui.common.safeSpacer
import com.github.calo001.hazel.ui.theme.Lato
import com.github.calo001.hazel.util.PainterIdentifier

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun AnimalsView(
    animals: List<Animal>,
    onClickAnimal: (Animal) -> Unit,
    onBackClick: () -> Unit,
    painterIdentifier: PainterIdentifier,
    speechStatus: SpeechStatus,
    onSpeechClick: () -> Unit,
    onTextChangeSpeech: (String) -> Unit,
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
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn {
            safeSpacer(extraSpace = 100.dp)

            val items = if (querySearch.isNotEmpty()) {
                animals.filter {
                    it.name.lowercase().contains(querySearch.lowercase())
                }
            } else {
                animals
            }
            items(
                count = items.size,
                key = { index -> items[index].id }
                ) { index ->
                val painter = painterIdentifier.getPainter(identifier = items[index].emojiCode)
                TextImageRow(
                    text = items[index].name,
                    image = painter,
                    modifier = Modifier
                        .clickable { onClickAnimal(items[index]) }
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
                title = "Animals",
                subtitle = "Basic vocabulary",
                onBackClick = onBackClick,
                onTextChange = {
                    querySearch = it
                },
                speechStatus = speechStatus,
                onSpeechClick = onSpeechClick,
                onTextChangeSpeech = onTextChangeSpeech
            )
        }
    }
}

@Composable
fun TextImageColumn(
    modifier: Modifier = Modifier,
    text: String,
    phonetic: String,
    image: Painter, 
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Image(
            painter = image, 
            contentDescription = null,
            modifier = Modifier.size(240.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = phonetic,
            style = LocalTextStyle.current.copy(
                fontFamily = Lato
            ),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}