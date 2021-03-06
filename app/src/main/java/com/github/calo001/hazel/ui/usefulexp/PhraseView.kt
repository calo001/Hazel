package com.github.calo001.hazel.ui.usefulexp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.github.calo001.hazel.model.hazeldb.Phrase
import com.github.calo001.hazel.model.status.TextToSpeechStatus
import com.github.calo001.hazel.ui.common.HazelToolbarButton
import com.github.calo001.hazel.ui.common.HazelToolbarMiniButton
import com.github.calo001.hazel.ui.theme.Lato
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun PhraseView(
    currentPhrase: Phrase?,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onListenClick: () -> Unit,
    onNavigate: () -> Unit,
    onShareClick: () -> Unit,
    hideNext: Boolean,
    hidePrevious: Boolean,
    textToSpeechStatus: TextToSpeechStatus,
) {
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val coroutineScope = rememberCoroutineScope()

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetBackgroundColor = MaterialTheme.colors.primarySurface,
        sheetContent = {
            val currentHeight = LocalConfiguration.current.screenHeightDp
            val heightSheet = if (currentHeight < 500) {
                LocalConfiguration.current.screenHeightDp.dp
            } else {
                LocalConfiguration.current.screenHeightDp.dp / 3
            }
            ConstraintLayout(
                Modifier
                    .fillMaxWidth()
                    .height(heightSheet)
                    .padding(16.dp)
            ) {
                val (title, howToUse, close) = createRefs()
                HazelToolbarMiniButton(
                    onClick = {
                        coroutineScope.launch {
                            if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                                bottomSheetScaffoldState.bottomSheetState.expand()
                            } else {
                                bottomSheetScaffoldState.bottomSheetState.collapse()
                            }
                        }
                    },
                    icon = Icons.Filled.Close,
                    modifier = Modifier.constrainAs(close) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                    }
                )
                Text(
                    text = "How to use this phrase",
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier
                        .constrainAs(title) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                        }
                )

                Box(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxSize()
                        .background(
                            color = Color.White.copy(
                                alpha = 0.4f
                            ),
                            shape = MaterialTheme.shapes.large
                        )
                        .padding(16.dp)
                        .constrainAs(howToUse) {
                            centerHorizontallyTo(parent)
                            top.linkTo(title.bottom)
                        }
                ) {
                    SelectionContainer {
                        Text(
                            text = currentPhrase?.howToUse ?: "",
                            style = LocalTextStyle.current.copy(
                                fontFamily = Lato,
                                fontSize = 18.sp
                            ),
                        )
                    }
                }
            }
        }, sheetPeekHeight = 0.dp
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (toolbar, phrase, controls) = createRefs()
            PhraseToolbar(
                onNavigate = onNavigate,
                onHelp = {
                    coroutineScope.launch {
                        if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                            bottomSheetScaffoldState.bottomSheetState.expand()
                        } else {
                            bottomSheetScaffoldState.bottomSheetState.collapse()
                        }
                    }
                },
                onShareClick = onShareClick,
                modifier = Modifier
                    .padding(16.dp)
                    .constrainAs(toolbar) {
                        centerHorizontallyTo(parent)
                        top.linkTo(parent.top)
                    }
            )
            SelectionContainer(
                modifier = Modifier.constrainAs(phrase) {
                    centerHorizontallyTo(parent)
                    top.linkTo(toolbar.bottom)
                    bottom.linkTo(controls.top)
                }
            ) {
                Text(
                    text = currentPhrase?.expression ?: "",
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier
                        .padding(16.dp)
                )
            }
            ControlsItem(
                onPreviousClick = onPreviousClick,
                onNextClick = onNextClick,
                onListenClick = onListenClick,
                hideNext = hideNext,
                hidePrevious = hidePrevious,
                modifier = Modifier.constrainAs(controls) {
                    centerHorizontallyTo(parent)
                    bottom.linkTo(parent.bottom)
                },
                textToSpeechStatus = textToSpeechStatus,
            )
        }
    }


}

@ExperimentalMaterialApi
@Composable
fun ControlsItem(
    modifier: Modifier = Modifier,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onListenClick: () -> Unit,
    hideNext: Boolean,
    hidePrevious: Boolean,
    textToSpeechStatus: TextToSpeechStatus,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {

        HazelToolbarButton(
            icon = Icons.Filled.ArrowBack,
            onClick = onPreviousClick,
            enabled = !hidePrevious,
            modifier = Modifier.padding(16.dp)
        )


        ExtendedFloatingActionButton(
            text = {
                when (textToSpeechStatus) {
                    TextToSpeechStatus.Playing -> {
                        val composition by rememberLottieComposition(
                            LottieCompositionSpec.RawRes(com.github.calo001.hazel.R.raw.sound_wave)
                        )
                        LottieAnimation(
                            composition = composition,
                            contentScale = ContentScale.FillHeight,
                            modifier = Modifier
                                .height(48.dp)
                                .width(56.dp)
                        )
                    }
                    TextToSpeechStatus.Loading -> {
                        val composition by rememberLottieComposition(
                            LottieCompositionSpec.RawRes(com.github.calo001.hazel.R.raw.loading_drop)
                        )
                        LottieAnimation(
                            composition = composition,
                            contentScale = ContentScale.FillHeight,
                            modifier = Modifier
                                .height(48.dp)
                                .width(56.dp)
                        )
                    }
                    TextToSpeechStatus.Finished,
                    TextToSpeechStatus.NoPlaying,
                    TextToSpeechStatus.Paused,
                    TextToSpeechStatus.Stopped -> {
                        Icon(
                            imageVector = Icons.Filled.VolumeUp,
                            contentDescription = "Listen",
                            Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            },
            onClick = {
                if(textToSpeechStatus !is TextToSpeechStatus.Playing ||
                    textToSpeechStatus !is TextToSpeechStatus.Loading) {
                        onListenClick()
                }
            },
        )

        HazelToolbarButton(
            icon = Icons.Filled.ArrowForward,
            onClick = onNextClick,
            enabled = !hideNext,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun PhraseToolbar(
    modifier: Modifier = Modifier,
    onNavigate: () -> Unit,
    onHelp: () -> Unit,
    onShareClick: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        HazelToolbarButton(
            icon = Icons.Filled.ArrowBack,
            onClick = onNavigate
        )
        Row {
            HazelToolbarButton(
                icon = Icons.Filled.Share,
                onClick = onShareClick,
                modifier = Modifier
            )
            Spacer(modifier = Modifier.size(16.dp))
            HazelToolbarButton(
                icon = Icons.Filled.Help,
                onClick = onHelp
            )
        }
    }
}

@ExperimentalMaterialApi
@Preview(showSystemUi = true)
@Composable
fun PhraseViewPreview() {
    PhraseView(
        currentPhrase = Phrase(
            id = "",
            expression = "hello again",
            howToUse = "Used any time"
        ),
        onPreviousClick = {},
        onNextClick = {},
        onListenClick = {},
        hideNext = false,
        hidePrevious = false,
        onNavigate = {},
        onShareClick = {},
        textToSpeechStatus = TextToSpeechStatus.NoPlaying,
    )
}