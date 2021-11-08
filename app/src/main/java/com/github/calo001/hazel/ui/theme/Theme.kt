package com.github.calo001.hazel.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.github.calo001.hazel.config.ColorVariant

@Composable
fun HazelTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colorVariant: ColorVariant,
    content: @Composable() () -> Unit,
) {
    val colors = if (darkTheme) {
        colorVariant.darkColors
    } else {
        colorVariant.lightColors
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}