package com.github.calo001.hazel.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CircleColor(
    modifier: Modifier = Modifier,
    color: Color,
    borderColor: Color = Color.Black
) {
    Box(
        modifier = modifier
            .border(
                width = 4.dp,
                color = borderColor,
                shape = CircleShape
            )
            .background(
                color = color,
                shape = CircleShape
            )
    )
}