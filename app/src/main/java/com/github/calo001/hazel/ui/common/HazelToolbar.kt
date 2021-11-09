package com.github.calo001.hazel.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun HazelToolbar() {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier.fillMaxWidth()
    ) {
        HazelToolbarButton(
            icon = Icons.Filled.DarkMode,
            onClick = {},
            modifier = Modifier
                .padding(vertical = 8.dp)
        )

        HazelToolbarButton(
            icon = Icons.Filled.Settings,
            onClick = {},
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun HazelToolbarButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    onClick: () -> Unit,
    enabled: Boolean = true,
) {
    Box(modifier = modifier) {
        IconButton(
            onClick = onClick,
            enabled = enabled,
            modifier = modifier
                .background(
                    color = MaterialTheme.colors.primaryVariant,
                    shape = CircleShape
                )
                .size(48.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null)
        }
    }
}

@Composable
fun HazelToolbarMiniButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    Box(modifier = modifier) {
        FloatingActionButton(
            onClick = onClick,
            backgroundColor = MaterialTheme.colors.primaryVariant,
            modifier = modifier.size(32.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null)
        }
    }
}