package com.github.calo001.hazel.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.github.calo001.hazel.config.ColorVariant
import com.github.calo001.hazel.ui.theme.HazelTheme


@ExperimentalMaterialApi
@Composable
fun BigCard(
    title: String,
    shapeLabel: @Composable () -> Unit,
    imageContent: @Composable () -> Unit,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(16.dp)
                .padding(end = 8.dp)
                .fillMaxSize()
        ) {
            val (text, shape) = createRefs()
            val widthConstraint = (LocalConfiguration.current.screenWidthDp / 2) - 32

            Text(
                text = title,
                style = MaterialTheme.typography.h4,
                modifier = Modifier
                    .width(widthConstraint.dp)
                    .constrainAs(text) {
                        centerVerticallyTo(parent)
                        start.linkTo(parent.start)
                    }
            )
            RoundedRotated(
                shapeLabel = shapeLabel,
                imageContent = imageContent,
                modifier = Modifier
                    .fillMaxHeight(0.7f)
                    .offset {
                        IntOffset(x = 32, y = 0)
                    }
                    .constrainAs(shape) {
                        end.linkTo(parent.end)
                        centerVerticallyTo(parent)
                    }
            )
        }

    }
}

@ExperimentalMaterialApi
@Preview(widthDp = 600)
@Composable
fun BigCardPreview() {
    HazelTheme(colorVariant = ColorVariant.Green) {
        BigCard(
            "The weather",
            { Text(text = "10º") },
            {},
        ) {}
    }
}