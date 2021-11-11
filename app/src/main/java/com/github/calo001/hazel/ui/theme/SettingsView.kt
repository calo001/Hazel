package com.github.calo001.hazel.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Palette
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.calo001.hazel.config.ColorVariant
import com.github.calo001.hazel.ui.common.HazelToolbarSimple

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun SettingsView(
    onBackClick: () -> Unit,
    colorVariant: ColorVariant,
    dictionaries: Dictionaries,
    onSelectColorScheme: (ColorVariant) -> Unit,
    onSelectDictionary: (Dictionaries) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        HazelToolbarSimple(
            title = "Settings",
            subtitle = "Main",
            onBackClick = onBackClick,
        )

        ListItem(
            icon = { Icon(
                imageVector = Icons.Filled.Book,
                contentDescription = null,
                modifier = Modifier
            ) },
            text = { Text(text = "Dictionary") },
            secondaryText = { Text(text = "Select a dictionary to open when click an external link.")  },
            modifier = Modifier.fillMaxWidth()
        )

        RadioGroupDictionaries(
            selectedDictionary = dictionaries,
            onSelectDictionary = onSelectDictionary,
        )

        ListItem(
            icon = { Icon(
                imageVector = Icons.Filled.Palette,
                contentDescription = null,
                modifier = Modifier
            ) },
            text = { Text(text = "Color scheme") },
            secondaryText = { Text(text = "Select a color scheme to chance color in the app.")  },
            modifier = Modifier.fillMaxWidth()
        )

        ColorSchemeGroup(
            selected = colorVariant,
            onSelectColorScheme = onSelectColorScheme
        )
    }
}

@Composable
private fun ColorSchemeGroup(
    selected: ColorVariant,
    onSelectColorScheme: (ColorVariant) -> Unit,
) {
    val colorSchemes = listOf(
        ColorVariant.Green,
        ColorVariant.Blue
    )
    Row(modifier = Modifier
        .padding(start = 60.dp)
        .padding(end = 8.dp)
        .padding(vertical = 8.dp)
    ) {
        colorSchemes.forEach { color ->
            val background = if (selected == color) {
                MaterialTheme.colors.surface
            } else {
                Color.Transparent
            }
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = background,
                modifier = Modifier
                    .padding(4.dp)
            ) {
                Box(modifier = Modifier.clickable { onSelectColorScheme(color) }) {
                    when(color) {
                        ColorVariant.Blue -> BlueCircle()
                        ColorVariant.Green -> GreenCircle()
                    }
                }
            }
        }
    }
}

@Composable
private fun RadioGroupDictionaries(
    selectedDictionary: Dictionaries,
    onSelectDictionary: (Dictionaries) -> Unit,
) {
    val dictionaries = listOf(
        Dictionaries.Cambridge,
        Dictionaries.Oxford,
        Dictionaries.WordReference
    )
    Column(modifier = Modifier
        .padding(start = 60.dp)
        .padding(end = 8.dp)
        .padding(vertical = 8.dp)
    ) {
        dictionaries.forEach { dictionary ->
            val background = if (selectedDictionary == dictionary) {
                MaterialTheme.colors.surface
            } else {
                Color.Transparent
            }
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = background,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onSelectDictionary(dictionary) }
                ) {
                    RadioButton(
                        selected = selectedDictionary == dictionary,
                        onClick = { onSelectDictionary(dictionary) },
                        colors = RadioButtonDefaults.colors(
                            unselectedColor = MaterialTheme.colors.surface,
                            selectedColor = MaterialTheme.colors.onPrimary
                        )
                    )
                    Text(
                        text = dictionary.name,
                        color = contentColorFor(backgroundColor = background)
                    )
                }
            }
        }
    }
}

@Composable
private fun GreenCircle() {
    val borderGreen = MaterialTheme.colors.onPrimary
    val colorsCircleGreen = listOf(
        LightGreen50,
        LightGreen100,
        LightGreen200,
        LightGreenA100
    )
    ColorCircle(
        border = borderGreen,
        palette = colorsCircleGreen
    )
}

@Composable
private fun BlueCircle() {
    val borderGreen = MaterialTheme.colors.onPrimary
    val colorsCircleGreen = listOf(
        Cyan50,
        Cyan100,
        Cyan200,
        CyanA100
    )
    ColorCircle(
        border = borderGreen,
        palette = colorsCircleGreen
    )
}

@Composable
fun ColorCircle(border: Color, palette: List<Color>) {
    val circleSize = 50.dp
    val sectionSize = circleSize / 2
    Surface(
        shape = CircleShape,
        modifier = Modifier
            .padding(16.dp)
            .size(circleSize)
            .border(width = 2.dp, color = border, shape = CircleShape)
    ) {
        Column {
            Row {
                Box(modifier = Modifier
                    .size(sectionSize)
                    .background(color = palette.first())
                )
                Box(modifier = Modifier
                    .size(sectionSize)
                    .background(color = palette[1])
                )
            }
            Row {
                Box(modifier = Modifier
                    .size(sectionSize)
                    .background(color = palette[2])
                )
                Box(modifier = Modifier
                    .size(sectionSize)
                    .background(color = palette[3])
                )
            }
        }
    }
}

sealed class Dictionaries(val name: String, val url: String) {
    object Cambridge: Dictionaries(
        name = "Cambridge Dictionary",
        url = "https://dictionary.cambridge.org/us/dictionary/english/"
    )
    object Oxford: Dictionaries(
        name = "Oxford Learner's Dictionaries",
        url = "https://www.oxfordlearnersdictionaries.com/definition/english/"
    )
    object WordReference: Dictionaries(
        name = "WordReference",
        url = "https://www.wordreference.com/definition/"
    )
}

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Preview(showSystemUi = true)
@Composable
fun SettingsViewPreview() {
    HazelTheme(colorVariant = ColorVariant.Green) {
        SettingsView(
            onBackClick = {},
            onSelectColorScheme = {},
            onSelectDictionary = {},
            colorVariant = ColorVariant.Green,
            dictionaries = Dictionaries.Oxford,
        )
    }
}

