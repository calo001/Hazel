package com.github.calo001.hazel.ui.countries

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.calo001.hazel.model.hazeldb.Country
import com.github.calo001.hazel.ui.animals.TextImageRow
import com.github.calo001.hazel.ui.common.HazelToolbarContent
import com.github.calo001.hazel.ui.common.SurfaceToolbar
import com.github.calo001.hazel.ui.common.safeSpacer
import com.github.calo001.hazel.util.PainterIdentifier

@ExperimentalComposeUiApi
@Composable
fun CountryView(
    countries: List<Country>,
    painterIdentifier: PainterIdentifier,
    onClickCountry: (Country) -> Unit,
    onBackClick: () -> Unit,
) {
    var querySearch by rememberSaveable { mutableStateOf("") }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn {
            safeSpacer(extraSpace = 100.dp)

            val items = if (querySearch.isNotEmpty()) {
                countries.filter {
                    it.name.lowercase().contains(querySearch.lowercase())
                }
            } else {
                countries
            }
            items(items.size) { index ->
                val painter = painterIdentifier.getPainter(identifier = items[index].emojiCode)
                TextImageRow(
                    text = items[index].name,
                    image = painter,
                    modifier = Modifier
                        .clickable { onClickCountry(items[index]) }
                        .padding(vertical = 18.dp)
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                )
            }
        }

        SurfaceToolbar {
            HazelToolbarContent(
                title = "Countries",
                subtitle = "Basic vocabulary",
                onBackClick = onBackClick,
                onTextChange = {
                    querySearch = it
                }
            )
        }
    }
}