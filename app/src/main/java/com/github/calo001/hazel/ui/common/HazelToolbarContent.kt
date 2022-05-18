package com.github.calo001.hazel.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.calo001.hazel.model.status.SpeechStatus

@ExperimentalComposeUiApi
@Composable
fun HazelToolbarContent(
    title: String,
    subtitle: String,
    onBackClick: () -> Unit,
    onTextChange: (String) -> Unit,
    speechStatus: SpeechStatus,
    onSpeechClick: () -> Unit,
    onTextChangeSpeech: (String) -> Unit,
) {
    Column {
        HazelToolbarButton(
            icon = Icons.Filled.ArrowBack,
            onClick = onBackClick,
            modifier = Modifier.padding(8.dp)
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Text(
            text = title,
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.size(18.dp))
        SearchBar(
            placeholder = "Search in $title",
            onTextChange = onTextChange,
            modifier = Modifier.padding(horizontal = 16.dp),
            speechStatus = speechStatus,
            onSpeechClick = onSpeechClick,
            onTextChangeSpeech = onTextChangeSpeech,
        )
    }
}

@ExperimentalComposeUiApi
@Preview
@Composable
fun HazelToolbarContentPreview() {
    HazelToolbarContent(
        title = "Apologizing",
        subtitle = "Useful expressions",
        onBackClick = {},
        onTextChange = {},
        onSpeechClick = {},
        speechStatus = SpeechStatus.NoSpeech,
        onTextChangeSpeech = {},
    )
}