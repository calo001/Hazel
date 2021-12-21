package com.github.calo001.hazel.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HazelToolbarWeather(
    onNavBack: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        HazelToolbarButton(
            icon = Icons.Filled.ArrowBack,
            onClick = onNavBack,
            modifier = Modifier
                .padding(vertical = 8.dp)
        )
        HazelToolbarButton(
            icon = Icons.Filled.Share,
            onClick = onShareClick,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun HazelToolbarWeatherPreview() {
    HazelToolbarWeather(
        onNavBack = {},
        onShareClick = {},
    )
}