package com.github.calo001.hazel.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HazelToolbarClock(
    onNavBack: () -> Unit,
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
    }
}

@Preview
@Composable
fun HazelToolbarClockPreview() {
    HazelToolbarClock(
        onNavBack = {},
        modifier = Modifier
    )
}
