package com.github.calo001.hazel.ui.usefulexp

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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.calo001.hazel.model.Phrase
import com.github.calo001.hazel.model.UsefulPhrase
import com.github.calo001.hazel.ui.common.HazelToolbarContent
import com.github.calo001.hazel.ui.common.SurfaceToolbar
import com.github.calo001.hazel.ui.common.safeSpacer

@ExperimentalComposeUiApi
@Composable
fun UsefulExpressionsView(
    usefulPhrase: UsefulPhrase,
    onClickPhrase: (Phrase) -> Unit,
    onBackClick: () -> Unit,
) {
    var querySearch by rememberSaveable { mutableStateOf("") }
    Box {
        LazyColumn {
            safeSpacer(extraSpace = 100.dp)

            val items = if (querySearch.isNotEmpty()) {
                usefulPhrase.phrases.filter {
                    it.expression.lowercase().contains(querySearch.lowercase())
                }
            } else {
                usefulPhrase.phrases
            }
            items(items.size) { index ->
                Text(
                    text = items[index].expression,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .clickable { onClickPhrase(items[index]) }
                        .padding(vertical = 24.dp)
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                )
            }
        }

        SurfaceToolbar {
            HazelToolbarContent(
                title = usefulPhrase.category,
                subtitle = "Useful phrases",
                onBackClick = onBackClick,
                onTextChange = {
                    querySearch = it
                }
            )
        }
    }
}