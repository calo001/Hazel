package com.github.calo001.hazel.ui.countries

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.github.calo001.hazel.R
import com.github.calo001.hazel.model.hazeldb.Country
import com.github.calo001.hazel.ui.theme.Lato
import com.github.calo001.hazel.util.PainterIdentifier

@ExperimentalAnimationApi
@Composable
fun CountryInfo(
    country: Country,
    modifier: Modifier = Modifier,
    painterIdentifier: PainterIdentifier,
    selectedInfo: CountryData,
    onSelectedInfoChange: (CountryData) -> Unit,
) {
    val menu = listOf(
        CountryData.Name,
        CountryData.Nationality,
        CountryData.Language,
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        TabBarCountry(
            menu = menu,
            selectedInfo = selectedInfo,
            onSelectedInfoChange = { onSelectedInfoChange(it) }
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

sealed class CountryData(val name: String) {
    object Name: CountryData("Country")
    object Nationality: CountryData("Nationality")
    object Language: CountryData("Language")
}