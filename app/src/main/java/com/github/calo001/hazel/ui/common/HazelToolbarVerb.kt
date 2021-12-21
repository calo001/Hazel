package com.github.calo001.hazel.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HazelToolbarVerb(
    onNavBack: () -> Unit,
    onOpenLink: () -> Unit,
    onSeeExamples: () -> Unit,
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
                icon = Icons.Filled.Share,
                onClick = onShareClick,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(vertical = 8.dp)
            )

            HazelToolbarButton(
                icon = Icons.Filled.FormatListBulleted,
                label = "e.g.",
                onClick = onSeeExamples,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

@Preview
@Composable
fun HazelToolbarVerbPreview() {
    HazelToolbarVerb(
        onShareClick = {},
        onNavBack = {},
        onOpenLink = {},
        onSeeExamples = {},
    )
}