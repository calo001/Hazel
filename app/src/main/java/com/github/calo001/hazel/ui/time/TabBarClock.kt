package com.github.calo001.hazel.ui.time

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@ExperimentalAnimationApi
@Composable
fun TabBarClock(
    modifier: Modifier = Modifier,
    menu: List<ClockForm>,
    selectedForm: ClockForm,
    onSelectedInfoChange: (ClockForm) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = menu.indexOf(selectedForm),
        backgroundColor = Color.Transparent,
        divider = { Box(modifier = Modifier) },
        indicator = { Box(modifier = Modifier) },
        modifier = modifier
    ) {
        menu.forEach { item ->
            Surface(
                shape = MaterialTheme.shapes.large,
                color = Color.Transparent,
            ) {
                Tab(
                    selected = item == selectedForm,
                    onClick = { onSelectedInfoChange(item) },
                    modifier = modifier
                ) {
                    AnimatedVisibility(item == selectedForm) {
                        Text(
                            text = item.name,
                            color = MaterialTheme.colors.onPrimary,
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colors.primarySurface,
                                    shape = MaterialTheme.shapes.large
                                )
                                .padding(12.dp)
                        )
                    }
                    AnimatedVisibility(visible = item != selectedForm) {
                        Text(
                            text = item.name,
                            modifier = Modifier
                                .background(
                                    color = Color.Transparent,
                                    shape = MaterialTheme.shapes.large
                                )
                                .padding(12.dp)
                        )
                    }
                }
            }
        }
    }
}