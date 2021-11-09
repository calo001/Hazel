package com.github.calo001.hazel.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Launch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HazelToolbarOneColor(
    onNavBack: () -> Unit,
    onOpenLink: () -> Unit,
    onSeeExamples: () -> Unit,
    onGallery: () -> Unit,
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

        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            HazelToolbarButton(
                icon = Icons.Filled.Launch,
                onClick = onOpenLink,
                modifier = Modifier
                    .padding(vertical = 8.dp)
            )

            HazelToolbarButton(
                icon = Icons.Filled.FormatListBulleted,
                label = "e.g.",
                onClick = onSeeExamples,
                modifier = Modifier.padding(8.dp)
            )

            HazelToolbarButton(
                icon = Icons.Filled.Collections,
                onClick = onGallery,
                modifier = Modifier
                    .padding(vertical = 8.dp)
            )
        }
    }
}

@Preview
@Composable
fun HazelToolbarOneColorPreview() {
    HazelToolbarOneColor(
        onNavBack = {},
        onOpenLink = {},
        onSeeExamples = {},
        onGallery = {},
        modifier = Modifier
    )
}

