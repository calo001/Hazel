package com.github.calo001.hazel.ui.colors

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.calo001.hazel.config.ColorVariant
import com.github.calo001.hazel.model.hazeldb.ColorHazel
import com.github.calo001.hazel.ui.common.CircleColor
import com.github.calo001.hazel.ui.common.HazelToolbarContent
import com.github.calo001.hazel.ui.common.SurfaceToolbar
import com.github.calo001.hazel.ui.common.safeSpacer
import com.github.calo001.hazel.ui.theme.HazelTheme
import com.github.calo001.hazel.util.parse

@ExperimentalComposeUiApi
@Composable
fun ColorsView(
    modifier: Modifier = Modifier,
    colorHazels: List<ColorHazel>,
    onClickColor: (ColorHazel) -> Unit,
    onBackClick: () -> Unit,
) {
    var querySearch by rememberSaveable { mutableStateOf("") }
    Box(
        modifier = modifier
            .background(MaterialTheme.colors.background)
            .fillMaxSize()
    ) {
        LazyColumn {
            safeSpacer(extraSpace = 100.dp)

            val items = if (querySearch.isNotEmpty()) {
                colorHazels.filter {
                    it.name.lowercase().contains(querySearch.lowercase())
                }
            } else {
                colorHazels
            }
            items(items.size) { index ->
                ItemColor(
                    colorHazel = items[index],
                    modifier = Modifier
                        .clickable { onClickColor(items[index]) }
                        .padding(vertical = 24.dp)
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                )
            }
        }

        SurfaceToolbar {
            HazelToolbarContent(
                title = "Colors",
                subtitle = "Basic vocabulary",
                onBackClick = onBackClick,
                onTextChange = {
                    querySearch = it
                }
            )
        }
    }
}

@Composable
fun ItemColor(
    modifier: Modifier = Modifier,
    colorHazel: ColorHazel,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        CircleColor(
            color = Color.parse(colorHazel.code),
            modifier = Modifier
                .size(40.dp)
        )
        Text(
            text = colorHazel.name,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Preview
@Composable
fun ItemColorPreview() {
    HazelTheme(colorVariant = ColorVariant.Green) {
        ItemColor(
            colorHazel = ColorHazel(code = "#FFFFFF", listOf(), "White", "White", "whites")
        )
    }
}