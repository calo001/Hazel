package com.github.calo001.hazel.ui.time

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.arbelkilani.clock.Clock
import com.arbelkilani.clock.enumeration.ClockType
import com.arbelkilani.clock.enumeration.analogical.DegreeType
import com.arbelkilani.clock.enumeration.analogical.DegreesStep
import com.github.calo001.hazel.R
import com.github.calo001.hazel.config.ColorVariant
import com.github.calo001.hazel.huawei.TextToSpeechStatus
import com.github.calo001.hazel.ui.common.CircleColor
import com.github.calo001.hazel.ui.common.HazelToolbarSimple
import com.github.calo001.hazel.ui.theme.HazelTheme
import com.github.calo001.hazel.ui.usefulexp.ControlsItem

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun TimeView(
    timeInWords: String,
    timeInNumbersPMAM: String,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onListen: (String) -> Unit,
    hasNext: Boolean,
    hasPrevious: Boolean,
    textToSpeechStatus: TextToSpeechStatus,
    onShareClick: () -> Unit,
) {
    var selectedForm by remember {
        mutableStateOf<ClockForm>(ClockForm.Analog)
    }
    val menu = listOf(
        ClockForm.Analog,
        ClockForm.Digital,
    )

    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (toolbar, tabBar, clock, content, controls) = createRefs()

        createVerticalChain(
            toolbar, tabBar, clock, content, controls,
            chainStyle = ChainStyle.SpreadInside
        )

        HazelToolbarSimple(
            onBackClick = onBackClick,
            title = "Time",
            subtitle = "Basic vocabulary",
            showShare= true,
            onShareClick = onShareClick,
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(toolbar) {
                    centerHorizontallyTo(parent)
                }
        )

        TabBarClock(
            menu = menu,
            selectedForm = selectedForm,
            onSelectedInfoChange = { selectedForm = it },
            modifier = Modifier
                .padding(start = 24.dp)
                .constrainAs(tabBar) {
                    centerHorizontallyTo(parent)
                }
        )

        ClockSurface(
            time = timeInNumbersPMAM,
            selectedForm = selectedForm,
            modifier = Modifier
                .size(220.dp)
                .constrainAs(clock) {
                    centerHorizontallyTo(parent)
                }
        )

        Text(
            text = timeInWords + "\n",
            style = MaterialTheme.typography.h4,
            textAlign = TextAlign.Center,
            maxLines = 2,
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .constrainAs(content) {
                    centerHorizontallyTo(parent)
                }
        )

        ControlsItem(
            onPreviousClick = onPreviousClick,
            onNextClick = onNextClick,
            onListenClick = { onListen(timeInWords) },
            hideNext = !hasNext,
            hidePrevious = !hasPrevious,
            textToSpeechStatus = textToSpeechStatus,
            modifier = Modifier.constrainAs(controls) {
                centerHorizontallyTo(parent)
            }
        )
    }
}

@Composable
private fun ClockSurface(
    modifier: Modifier = Modifier,
    selectedForm: ClockForm,
    time: String,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier,
    ) {
        CircleColor(
            color = MaterialTheme.colors.surface,
            modifier = Modifier
                .fillMaxSize()
        )
        AnimatedVisibility(visible = selectedForm is ClockForm.Analog) {
            AndroidView(
                factory = { context ->
                    Clock(context).apply {
                        setClockType(ClockType.analogical)
                        setShowSecondsNeedle(true)
                        setShowDegrees(true)
                        setDegreesType(DegreeType.circle)
                        setDegreesStep(DegreesStep.twelve)
                        setSecondsNeedleColor(R.color.black)
                        showCenter(true)
                        setCenterOuterColor(R.color.black)
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
            )
        }

        AnimatedVisibility(visible = selectedForm is ClockForm.Digital) {
            Text(
                text = time,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h3,
                color = Color.Black,
                modifier = Modifier
            )
        }
    }
}

sealed class ClockForm(val name: String) {
    object Analog: ClockForm("Analog")
    object Digital: ClockForm("Digital")
}

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Preview(showSystemUi = true)
@Composable
fun TimeViewPreview() {
    HazelTheme(colorVariant = ColorVariant.Green) {
        TimeView(
            onBackClick = {},
            onListen = {},
            onNextClick = {},
            onPreviousClick = {},
            hasNext = true,
            hasPrevious = true,
            textToSpeechStatus = TextToSpeechStatus.NoPlaying,
            timeInNumbersPMAM = "03:30\nPM",
            timeInWords = "Twelve o'clock",
            onShareClick = {},
        )
    }
}