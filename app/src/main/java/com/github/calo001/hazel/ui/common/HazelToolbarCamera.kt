package com.github.calo001.hazel.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Collections
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HazelToolbarCamera(
    modifier: Modifier = Modifier,
    onNavBack: () -> Unit,
    onGallery: () -> Unit,
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
            icon = Icons.Filled.Collections,
            onClick = onGallery,
            modifier = Modifier
                .padding(vertical = 8.dp)
        )
    }
}

@Preview
@Composable
fun HazelToolbarCameraPreview() {
    HazelToolbarCamera(
        onNavBack = {},
        modifier = Modifier,
        onGallery = {}
    )
}
