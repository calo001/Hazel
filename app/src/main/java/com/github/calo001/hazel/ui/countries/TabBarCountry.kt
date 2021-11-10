package com.github.calo001.hazel.ui.countries

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TabBarCountry(
    menu: List<CountryData>,
    selectedInfo: CountryData,
    onSelectedInfoChange: (CountryData) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = menu.indexOf(selectedInfo),
        backgroundColor = Color.Transparent,
        divider = { Box(modifier = Modifier) },
        indicator = { Box(modifier = Modifier) }
    ) {
        menu.forEach { item ->
            Surface(
                shape = MaterialTheme.shapes.large,
                color = Color.Transparent,
            ) {
                Tab(
                    selected = item == selectedInfo,
                    onClick = { onSelectedInfoChange(item) }
                ) {
                    AnimatedVisibility(item == selectedInfo) {
                        Text(
                            text = item.name,
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colors.primarySurface,
                                    shape = MaterialTheme.shapes.large
                                )
                                .padding(12.dp)
                                .fillMaxWidth()
                        )
                    }
                    AnimatedVisibility(visible = item != selectedInfo) {
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