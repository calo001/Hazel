package com.github.calo001.hazel.ui.countries

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.calo001.hazel.R
import com.github.calo001.hazel.model.hazeldb.Country
import com.github.calo001.hazel.ui.theme.Lato
import com.github.calo001.hazel.util.PainterIdentifier

@Composable
fun CountryInfo(
    country: Country,
    modifier: Modifier = Modifier,
    painterIdentifier: PainterIdentifier,
    selectedInfo: CountryData,
    onSelectedInfoChange: (CountryData) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        ToggleGroup(
            dataSelected = selectedInfo,
            onClick = { onSelectedInfoChange(it) }
        )
        when (selectedInfo) {
            CountryData.Language -> LanguageInfo(
                country = country,
                painterIdentifier = painterIdentifier
            )
            CountryData.Name -> CountryNameInfo(
                country = country,
                painterIdentifier = painterIdentifier
            )
            CountryData.Nationality -> NationalityInfo(
                country = country,
                painterIdentifier = painterIdentifier
            )
        }
    }
}

@Composable
fun NationalityInfo(
    country: Country,
    painterIdentifier: PainterIdentifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row {
            Image(
                painter = painterResource(
                    id = R.drawable.openmoji_1f468_200d_1f469_200d_1f467_200d_1f466
                ),
                contentDescription = null,
                modifier = Modifier
                    .height(240.dp)
                    .width(120.dp)
            )
            Image(
                painter = painterIdentifier.getPainter(identifier = country.emojiCode),
                contentDescription = null,
                modifier = Modifier
                    .height(240.dp)
                    .width(120.dp)
            )
        }
        Text(
            text = country.nationality,
            style = MaterialTheme.typography.h6,
            modifier = Modifier
        )
        Text(
            text = country.nationalityPhonetic,
            style = LocalTextStyle.current.copy(
                fontFamily = Lato
            ),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun LanguageInfo(
    country: Country,
    painterIdentifier: PainterIdentifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row {
            Image(
                painter = painterIdentifier.getPainter(identifier = country.emojiCode),
                contentDescription = null,
                modifier = Modifier
                    .height(240.dp)
                    .width(120.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.openmoji_1f4ac),
                contentDescription = null,
                modifier = Modifier
                    .height(240.dp)
                    .width(120.dp)
            )
        }
        Text(
            text = country.language,
            style = MaterialTheme.typography.h6,
            modifier = Modifier
        )
        Text(
            text = country.languagePhonetic,
            style = LocalTextStyle.current.copy(
                fontFamily = Lato
            ),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun CountryNameInfo(
    country: Country,
    painterIdentifier: PainterIdentifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterIdentifier.getPainter(identifier = country.emojiCode),
            contentDescription = null,
            modifier = Modifier.size(240.dp)
        )
        Text(
            text = country.name,
            style = MaterialTheme.typography.h6,
            modifier = Modifier
        )
        Text(
            text = country.countryPhonetic,
            style = LocalTextStyle.current.copy(
                fontFamily = Lato
            ),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun ToggleGroup(
    dataSelected: CountryData,
    onClick: (CountryData) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        ToggleButton(
            text = "Country",
            selected = dataSelected is CountryData.Name,
            onClick = { onClick(CountryData.Name) },
            isOnStart = true,
            isOnEnd = false,
        )
        ToggleButton(
            text = "Nationality",
            selected = dataSelected is CountryData.Nationality,
            isOnStart = false,
            isOnEnd = false,
            onClick = { onClick(CountryData.Nationality) }
        )
        ToggleButton(
            text = "Language",
            selected = dataSelected is CountryData.Language,
            isOnStart = false,
            isOnEnd = true,
            onClick = { onClick(CountryData.Language) }
        )
    }
}

@Composable
fun ToggleButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    isOnStart: Boolean,
    isOnEnd: Boolean
) {
    val shape = if (isOnStart) {
        MaterialTheme.shapes.large.copy(
            topEnd = CornerSize(0.dp),
            bottomEnd = CornerSize(0.dp),
        )
    } else if (isOnEnd) {
        MaterialTheme.shapes.large.copy(
            topStart = CornerSize(0.dp),
            bottomStart = CornerSize(0.dp),
        )
    } else {
        RectangleShape
    }
    if (selected) {
        Button(
            shape = shape,
            modifier = Modifier,
            onClick = onClick
        ) { Text(text = text) }
    } else {
        OutlinedButton(
            shape = shape,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colors.onPrimary,
                backgroundColor = MaterialTheme.colors.primaryVariant
            ),
            border = BorderStroke(0.dp, Color.Transparent),
            modifier = Modifier,
            onClick = onClick
        ) { Text(text = text) }
    }
}

@Preview
@Composable
fun ToggleGroupPreview() {
    ToggleGroup(
        CountryData.Nationality,
        onClick = {}
    )
}

sealed interface CountryData {
    object Name: CountryData
    object Nationality: CountryData
    object Language: CountryData
}