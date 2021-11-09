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

@ExperimentalComposeUiApi
@Composable
fun HazelToolbarColorExample(
    title: String,
    subtitle: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
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
    }
}

@ExperimentalComposeUiApi
@Preview
@Composable
fun HazelToolbarColorExamplePreview() {
    HazelToolbarColorExample(
        title = "Example",
        subtitle = "White",
        onBackClick = {},
        modifier = Modifier
    )
}