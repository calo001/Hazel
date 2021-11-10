package com.github.calo001.hazel.ui.usefulexp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.github.calo001.hazel.model.hazeldb.Phrase
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
    hideNext: Boolean,
    hidePrevious: Boolean,
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
                LocalConfiguration.current.screenHeightDp.dp / 2
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
                            color = MaterialTheme.colors.background.copy(0.6f),
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
                                fontFamily = Lato
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
                }
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
                Icon(
                    imageVector = Icons.Filled.VolumeUp,
                    contentDescription = "Listen",
                    Modifier.padding(horizontal = 16.dp)
                ) },
            onClick = onListenClick,
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
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        HazelToolbarButton(
            icon = Icons.Filled.ArrowBack,
            onClick = onNavigate
        )
        HazelToolbarButton(
            icon = Icons.Filled.Help,
            onClick = onHelp
        )
    }
}

@ExperimentalMaterialApi
@Preview(showSystemUi = true)
@Composable
fun PhraseViewPreview() {
    PhraseView(
        currentPhrase = Phrase(expression = "hello again", "Used any time"),
        onPreviousClick = {},
        onNextClick = {},
        onListenClick = {},
        hideNext = false,
        hidePrevious = false,
        onNavigate = {}
    )
}