package com.github.calo001.hazel.ui.settings

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Palette
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.calo001.hazel.config.ColorVariant
import com.github.calo001.hazel.config.DarkMode
import com.github.calo001.hazel.ui.common.HazelToolbarSimple
import com.github.calo001.hazel.ui.common.SurfaceToolbar
import com.github.calo001.hazel.ui.theme.*
import com.google.accompanist.flowlayout.FlowRow

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun SettingsView(
    onBackClick: () -> Unit,
    darkMode: DarkMode,
    colorVariant: ColorVariant,
    dictionaries: Dictionaries,
    onSelectColorScheme: (ColorVariant) -> Unit,
    onSelectDictionary: (Dictionaries) -> Unit,
    onSelectDarkMode: (DarkMode) -> Unit,
) {

    Box(modifier = Modifier
        .fillMaxSize()
    ) {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(180.dp))
            SettingsContent(
                dictionaries,
                onSelectDictionary,
                colorVariant,
                onSelectColorScheme,
                darkMode,
                onSelectDarkMode
            )
        }
        SurfaceToolbar {
            HazelToolbarSimple(
                title = "Settings",
                subtitle = "Main",
                onBackClick = onBackClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun SettingsContent(
    dictionaries: Dictionaries,
    onSelectDictionary: (Dictionaries) -> Unit,
    colorVariant: ColorVariant,
    onSelectColorScheme: (ColorVariant) -> Unit,
    darkMode: DarkMode,
    onSelectDarkMode: (DarkMode) -> Unit
) {
    ListItem(
        icon = {
            Icon(
                imageVector = Icons.Filled.Book,
                contentDescription = null,
                modifier = Modifier
            )
        },
        text = { Text(text = "Dictionary") },
        secondaryText = { Text(text = "Select a dictionary to open when click an external link.") },
        modifier = Modifier.fillMaxWidth()
    )

    RadioGroupDictionaries(
        selectedDictionary = dictionaries,
        onSelectDictionary = onSelectDictionary,
    )

    ListItem(
        icon = {
            Icon(
                imageVector = Icons.Filled.Palette,
                contentDescription = null,
                modifier = Modifier
            )
        },
        text = { Text(text = "Color scheme") },
        secondaryText = { Text(text = "Select a color scheme for the app.") },
        modifier = Modifier.fillMaxWidth()
    )

    ColorSchemeGroup(
        selected = colorVariant,
        onSelectColorScheme = onSelectColorScheme
    )

    ListItem(
        icon = {
            Icon(
                imageVector = Icons.Filled.DarkMode,
                contentDescription = null,
                modifier = Modifier
            )
        },
        text = { Text(text = "Dark mode") },
        secondaryText = { Text(text = "Select dark mode for the app.") },
        modifier = Modifier.fillMaxWidth()
    )

    DarkModeGroup(
        selected = darkMode,
        onSelectDarkMode = onSelectDarkMode
    )
}

@Composable
private fun DarkModeGroup(
    selected: DarkMode,
    onSelectDarkMode: (DarkMode) -> Unit,
) {
    val uiModes = listOf(
        DarkMode.Light,
        DarkMode.Dark,
        DarkMode.FollowSystem,
    )
    Row(modifier = Modifier
        .padding(start = 60.dp)
        .padding(end = 8.dp)
        .padding(vertical = 8.dp)
    ) {
        uiModes.forEach { uiMode ->
            val background = if (selected == uiMode) {
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
                Box(modifier = Modifier.clickable { onSelectDarkMode(uiMode) }) {
                    when(uiMode) {
                        DarkMode.Dark -> DarkCircle()
                        DarkMode.Light -> LightCircle()
                        DarkMode.FollowSystem -> AutoCircle()
                    }
                }
            }
        }
    }
}

@Composable
fun LightCircle() {
    val border = MaterialTheme.colors.onPrimary
    val colorsCircle = listOf(
        Color.White,
        Color.White,
        Color.White,
        Color.White
    )
    ColorCircle(
        border = border,
        palette = colorsCircle
    )
}

@Composable
fun AutoCircle() {
    val border = MaterialTheme.colors.onPrimary
    val colorsCircle = listOf(
        Color.White,
        Color.White,
        Color.Black,
        Color.Black
    )
    ColorCircle(
        border = border,
        palette = colorsCircle
    )
}

@Composable
fun DarkCircle() {
    val border = MaterialTheme.colors.onPrimary
    val colorsCircle = listOf(
        Color.Black,
        Color.Black,
        Color.Black,
        Color.Black
    )
    ColorCircle(
        border = border,
        palette = colorsCircle
    )
}

@Composable
private fun ColorSchemeGroup(
    selected: ColorVariant,
    onSelectColorScheme: (ColorVariant) -> Unit,
) {
    val colorSchemes = listOf(
        ColorVariant.Green,
        ColorVariant.Blue,
        ColorVariant.Pink,
        ColorVariant.Indigo,
        ColorVariant.Purple,
        ColorVariant.Amber,
    )
    FlowRow (modifier = Modifier
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
                        ColorVariant.Blue -> CyanCircle()
                        ColorVariant.Green -> LightGreenCircle()
                        ColorVariant.Amber -> AmberCircle()
                        ColorVariant.Indigo -> IndigoCircle()
                        ColorVariant.Pink -> PinkCircle()
                        ColorVariant.Purple -> PurpleCircle()
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
private fun LightGreenCircle() {
    val borderGreen = MaterialTheme.colors.onPrimary
    val colorsCircleGreen = listOf(
        Color.GreenA100,
        Color.LightGreen100,
        Color.LightGreen200,
        Color.LightGreenA100
    )
    ColorCircle(
        border = borderGreen,
        palette = colorsCircleGreen
    )
}

@Composable
private fun AmberCircle() {
    val borderGreen = MaterialTheme.colors.onPrimary
    val colorsCircleGreen = listOf(
        Color.OrangeA100,
        Color.Amber100,
        Color.Amber200,
        Color.AmberA100
    )
    ColorCircle(
        border = borderGreen,
        palette = colorsCircleGreen
    )
}

@Composable
private fun IndigoCircle() {
    val borderGreen = MaterialTheme.colors.onPrimary
    val colorsCircleGreen = listOf(
        Color.BlueA100,
        Color.Indigo100,
        Color.Indigo200,
        Color.IndigoA100
    )
    ColorCircle(
        border = borderGreen,
        palette = colorsCircleGreen
    )
}

@Composable
private fun PinkCircle() {
    val borderGreen = MaterialTheme.colors.onPrimary
    val colorsCircleGreen = listOf(
        Color.RedA100,
        Color.Pink100,
        Color.Pink200,
        Color.PinkA100
    )
    ColorCircle(
        border = borderGreen,
        palette = colorsCircleGreen
    )
}

@Composable
private fun PurpleCircle() {
    val borderGreen = MaterialTheme.colors.onPrimary
    val colorsCircleGreen = listOf(
        Color.DeepPurpleA100,
        Color.Pink100,
        Color.Pink200,
        Color.PinkA100
    )
    ColorCircle(
        border = borderGreen,
        palette = colorsCircleGreen
    )
}

@Composable
private fun CyanCircle() {
    val borderGreen = MaterialTheme.colors.onPrimary
    val colorsCircleGreen = listOf(
        Color.LightBlueA100,
        Color.Cyan100,
        Color.Cyan200,
        Color.CyanA100
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
            onSelectDarkMode = {},
            colorVariant = ColorVariant.Green,
            dictionaries = Dictionaries.Oxford,
            darkMode = DarkMode.FollowSystem,
        )
    }
}

