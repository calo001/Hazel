package com.github.calo001.hazel.ui.colors

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.github.calo001.hazel.model.status.TextToSpeechStatus
import com.github.calo001.hazel.ui.common.HazelToolbarSimple
import com.github.calo001.hazel.ui.usefulexp.ControlsItem

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun SimpleExamplesView(
    title: String,
    example: String,
    onBackClick: () -> Unit,
    hideNext: Boolean,
    hidePrevious: Boolean,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onListenClick: () -> Unit,
    modifier: Modifier = Modifier,
    textToSpeechStatus: TextToSpeechStatus,
) {
    ConstraintLayout(
        modifier = modifier.fillMaxSize()
    ) {
        val (toolbar, content, controls) = createRefs()
        HazelToolbarSimple(
            title = title,
            subtitle = "Examples with",
            onBackClick = onBackClick,
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(toolbar) {
                    centerHorizontallyTo(parent)
                    top.linkTo(parent.top)
                }
        )
        Text(
            text = example,
            style = MaterialTheme.typography.h4,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(16.dp)
                .constrainAs(content) {
                    centerHorizontallyTo(parent)
                    top.linkTo(toolbar.bottom)
                    bottom.linkTo(controls.top)
                }
        )
        ControlsItem(
            onPreviousClick = onPreviousClick,
            onNextClick = onNextClick,
            onListenClick = onListenClick,
            hideNext = hideNext,
            hidePrevious = hidePrevious,
            textToSpeechStatus = textToSpeechStatus,
            modifier = Modifier.constrainAs(controls) {
                centerHorizontallyTo(parent)
                bottom.linkTo(parent.bottom)
            }
        )
    }
}

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Preview
@Composable
fun ColorExamplesViewPreview() {
    SimpleExamplesView(
        title = "Blue",
        example = "Ejemplo 1",
        onBackClick = {},
        hideNext = false,
        hidePrevious = false,
        onPreviousClick = {},
        onNextClick = {},
        onListenClick = {},
        textToSpeechStatus = TextToSpeechStatus.NoPlaying,
    )
}