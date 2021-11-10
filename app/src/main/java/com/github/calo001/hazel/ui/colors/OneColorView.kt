package com.github.calo001.hazel.ui.colors

import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.github.calo001.hazel.model.hazeldb.ColorHazel
import com.github.calo001.hazel.ui.common.CircleColor
import com.github.calo001.hazel.ui.common.HazelToolbarOneColor
import com.github.calo001.hazel.ui.theme.Lato
import com.github.calo001.hazel.ui.usefulexp.ControlsItem
import com.github.calo001.hazel.util.complementaryBW
import com.github.calo001.hazel.util.parse

@ExperimentalMaterialApi
@Composable
fun OneColorView(
    colorHazel: ColorHazel,
    onNavBack: () -> Unit,
    onOpenLink: () -> Unit,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onSeeExamples: (ColorHazel) -> Unit,
    onGallery: (ColorHazel) -> Unit,
    onListen: (String) -> Unit,
    hasNext: Boolean,
    hasPrevious: Boolean,
) {
    ConstraintLayout {
        val (toolbar, content) = createRefs()

        HazelToolbarOneColor(
            onNavBack = onNavBack,
            onOpenLink = onOpenLink,
            onSeeExamples = { onSeeExamples(colorHazel) },
            onGallery = { onGallery(colorHazel) },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .constrainAs(toolbar) {
                    centerHorizontallyTo(parent)
                    top.linkTo(parent.top)
                }
        )
        OneColorContent(
            colorHazel = colorHazel,
            onPreviousClick = onPreviousClick,
            onNextClick = onNextClick,
            onListenClick = { onListen(colorHazel.name) },
            hasNext = hasNext,
            hasPrevious = hasPrevious,
            modifier = Modifier.constrainAs(content) {
                centerHorizontallyTo(parent)
                top.linkTo(toolbar.bottom)
                bottom.linkTo(parent.bottom)
            }
        )
    }
}

@ExperimentalMaterialApi
@Composable
private fun OneColorContent(
    colorHazel: ColorHazel,
    modifier: Modifier = Modifier,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onListenClick: () -> Unit,
    hasNext: Boolean,
    hasPrevious: Boolean,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            CircleColor(
                color = Color.parse(colorHazel.code),
                modifier = Modifier
                    .size(280.dp)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = colorHazel.name,
                    style = MaterialTheme.typography.h3,
                    color = Color.parse(colorHazel.code).complementaryBW
                )
                Text(
                    text = colorHazel.phonetic,
                    color = Color.parse(colorHazel.code).complementaryBW,
                    style = MaterialTheme.typography.caption.copy(
                        fontFamily = Lato
                    )
                )
            }
        }

        Spacer(modifier = Modifier.size(16.dp))
        ControlsItem(
            onPreviousClick = onPreviousClick,
            onNextClick = onNextClick,
            onListenClick = onListenClick,
            hideNext = !hasNext,
            hidePrevious = !hasPrevious,
        )
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
fun OneColorPreview() {
    OneColorView(
        colorHazel = ColorHazel(
            code = "#000000",
            name = "white",
            phonetic = "white",
            type = "whites",
            examples = listOf(),
        ),
        onNavBack = {},
        onOpenLink = {},
        onNextClick = {},
        onPreviousClick = {},
        onSeeExamples = {},
        onGallery = {},
        onListen = {},
        hasNext = false,
        hasPrevious = false,
    )
}