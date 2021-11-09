package com.github.calo001.hazel.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp

@Composable
fun SurfaceToolbar(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .background(
                brush = Brush.verticalGradient(
                    (0f to MaterialTheme.colors.background),
                    (0.95f to MaterialTheme.colors.background.copy(alpha = 0.9f)),
                    (1f to MaterialTheme.colors.background.copy(alpha = 0f)),
                )
            )
    ) {
        content()
    }
}