package com.github.calo001.hazel.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HazelToolbarCountry(
    onNavBack: () -> Unit,
    onOpenMap: () -> Unit,
    onGallery: () -> Unit,
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

        Row {
            HazelToolbarButton(
                icon = Icons.Filled.Map,
                onClick = onOpenMap,
                modifier = Modifier
                    .padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.size(8.dp))

            HazelToolbarButton(
                icon = Icons.Filled.Share,
                onClick = onShareClick,
                modifier = Modifier.padding(8.dp)
            )

            Spacer(modifier = Modifier.size(8.dp))

            HazelToolbarButton(
                icon = Icons.Filled.Collections,
                onClick = onGallery,
                modifier = Modifier
                    .padding(vertical = 8.dp)
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun HazelToolbarCountryPreview() {
    HazelToolbarAnimal(
        onGallery = {},
        onNavBack = {},
        onOpenLink = {},
        onShareClick = {},
    )
}