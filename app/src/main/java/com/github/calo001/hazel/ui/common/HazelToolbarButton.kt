package com.github.calo001.hazel.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun HazelToolbarButton(
    modifier: Modifier = Modifier,
    label: String = "",
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
            if (label.isNotEmpty()) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.button.copy(
                        color = MaterialTheme.colors.onPrimary
                    )
                )
            } else {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onPrimary,
                )
            }
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