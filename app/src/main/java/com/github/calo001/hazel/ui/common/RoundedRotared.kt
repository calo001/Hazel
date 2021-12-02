package com.github.calo001.hazel.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp


@Composable
fun RoundedRotated(
    modifier: Modifier = Modifier,
    shapeLabel: String,
    image: Painter
) {
    Surface(
        shape = RoundedCornerShape(80.dp),
        color = MaterialTheme.colors.secondary,
        modifier = modifier
            .rotate(-45f)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(modifier = Modifier.size(16.dp))
            Image(
                painter = image,
                contentDescription = null,
                modifier = Modifier
                    .rotate(45f)
            )
            Text(
                text = shapeLabel,
                style = MaterialTheme.typography.h4,
                modifier = Modifier
                    .rotate(45f)
            )
            Spacer(modifier = Modifier.size(16.dp))
        }
    }
}