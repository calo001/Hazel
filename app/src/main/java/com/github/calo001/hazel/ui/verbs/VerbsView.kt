package com.github.calo001.hazel.ui.verbs

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.calo001.hazel.huawei.SpeechStatus
import com.github.calo001.hazel.model.hazeldb.Verb
import com.github.calo001.hazel.ui.ads.SimpleRoundedBanner
import com.github.calo001.hazel.ui.common.HazelToolbarContent
import com.github.calo001.hazel.ui.common.SurfaceToolbar
import com.github.calo001.hazel.ui.common.TextImageRow
import com.github.calo001.hazel.ui.common.safeSpacer
import com.github.calo001.hazel.util.PainterIdentifier

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun VerbsView(
    title: String,
    verbs: List<Verb>,
    onClickVerb: (Verb) -> Unit,
    onBackClick: () -> Unit,
    painterIdentifier: PainterIdentifier,
    speechStatus: SpeechStatus,
    onSpeechClick: () -> Unit,
    onTextChangeSpeech: (String) -> Unit,
) {
    var verbSearch by rememberSaveable { mutableStateOf("") }
    if (speechStatus is SpeechStatus.Result) {
        val inputSpeech = speechStatus.text.trim()
        if (inputSpeech.isNotEmpty()) {
            onTextChangeSpeech(inputSpeech)
            verbSearch = inputSpeech
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn {
            safeSpacer(extraSpace = 100.dp)

            val items = if (verbSearch.isNotEmpty()) {
                verbs.filter {
                    it.base.verb.lowercase().contains(verbSearch.lowercase())
                }
            } else {
                verbs
            }
            items(
                count = items.size,
                key = { index -> items[index].id }
            ) { index ->
                val painter = painterIdentifier.getPainter(identifier = items[index].emojiCode)
                TextImageRow(
                    text = items[index].base.verb,
                    image = painter,
                    modifier = Modifier
                        .clickable { onClickVerb(items[index]) }
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
                title = title,
                subtitle = "Basic vocabulary",
                onBackClick = onBackClick,
                onTextChange = {
                    verbSearch = it
                },
                onSpeechClick = onSpeechClick,
                speechStatus = speechStatus,
                onTextChangeSpeech = onTextChangeSpeech,
            )
        }
    }
}