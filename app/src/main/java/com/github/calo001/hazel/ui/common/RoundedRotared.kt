package com.github.calo001.hazel.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp


@Composable
fun RoundedRotated(
    modifier: Modifier = Modifier,
    shapeLabel: @Composable () -> Unit,
    imageContent: @Composable () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(80.dp),
        color = MaterialTheme.colors.background.copy(alpha = 0.8f),
        modifier = modifier
            .rotate(-45f)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(modifier = Modifier.size(16.dp))
            imageContent()
            shapeLabel()
            Spacer(modifier = Modifier.size(16.dp))
        }
    }
}