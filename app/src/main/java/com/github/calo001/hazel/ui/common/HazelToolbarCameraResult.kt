package com.github.calo001.hazel.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FileCopy
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HazelToolbarCameraResult(
    onNavBack: () -> Unit,
    onCopy: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        Column {
            HazelToolbarButton(
                icon = Icons.Filled.ArrowBack,
                onClick = onNavBack,
                modifier = Modifier
                    .padding(vertical = 8.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = "Basic vocabulary",
                style = MaterialTheme.typography.caption,
                modifier = Modifier
            )
            Text(
                text = "Results",
                style = MaterialTheme.typography.h5,
                modifier = Modifier
            )
            Spacer(modifier = Modifier.size(18.dp))
        }
        HazelToolbarButton(
            icon = Icons.Filled.FileCopy,
            onClick = onCopy,
            modifier = Modifier
                .padding(vertical = 8.dp)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun HazelToolbarCameraResultPreview() {
    HazelToolbarCameraResult(
        onNavBack = {},
        onCopy = {},
    )
}