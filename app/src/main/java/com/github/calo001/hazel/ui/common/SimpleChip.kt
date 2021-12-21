package com.github.calo001.hazel.ui.common

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.calo001.hazel.config.ColorVariant
import com.github.calo001.hazel.ui.theme.HazelTheme

@Composable
fun SimpleChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val transition = updateTransition(
        targetState = selected,
        label = "Transition"
    )

    val color by transition.animateColor(
        transitionSpec = { tween(400) },
        label = "Button Background Color"
    ) { state ->
        when (state) {
            true -> MaterialTheme.colors.surface
            false -> Color.Transparent
        }
    }

    val textColor by transition.animateColor(
        transitionSpec = { tween(400) },
        label = "Button Text Color"
    ) { state ->
        when (state) {
            true -> Color.Black
            false -> Color.White
        }
    }

    Surface(
        shape = MaterialTheme.shapes.large,
        color = color,
        modifier = Modifier
            .padding(8.dp)
    ) {
        Box(modifier = Modifier.clickable { onClick() }) {
            Text(
                text = text,
                style = MaterialTheme.typography.button,
                color = textColor,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Preview
@Composable
fun SimpleChipPreview() {
    HazelTheme(colorVariant = ColorVariant.Green) {
        Row {
            SimpleChip(text = "Uno", selected = true) {}
            SimpleChip(text = "Dos", selected = false) {}
        }
    }
}