package com.github.calo001.hazel.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun HazelToolbarButton(
    modifier: Modifier = Modifier,
    label: String = "",
    icon: ImageVector,
    onClick: () -> Unit,
    enabled: Boolean = true,
    background: Color = MaterialTheme.colors.primaryVariant
) {
    Box(modifier = modifier) {
        IconButton(
            onClick = onClick,
            enabled = enabled,
            modifier = modifier
                .background(
                    color = if (enabled) background else background.copy(alpha = 0.4f),
                    shape = CircleShape
                )
                .size(48.dp)
        ) {
            val colorOn = MaterialTheme.colors.onPrimary

            if (label.isNotEmpty()) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.button.copy(
                        color = if (enabled) colorOn else colorOn.copy(alpha = 0.4f)
                    )
                )
            } else {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (enabled) colorOn else colorOn.copy(alpha = 0.4f),
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