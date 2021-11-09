package com.github.calo001.hazel.ui.usefulexp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.calo001.hazel.model.Phrase
import com.github.calo001.hazel.model.UsefulPhrase
import com.github.calo001.hazel.ui.common.HazelToolbarContent
import com.github.calo001.hazel.ui.common.SurfaceToolbar
import com.github.calo001.hazel.ui.common.safeSpacer

@Composable
fun UsefulExpressionsView(
    usefulPhrase: UsefulPhrase,
    onClickPhrase: (Phrase) -> Unit,
) {
    Box {
        LazyColumn {
            safeSpacer(extraSpace = 100.dp)

            items(usefulPhrase.phrases.size) { index ->
                Text(
                    text = usefulPhrase.phrases[index].expression,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier
                        .clickable { onClickPhrase(usefulPhrase.phrases[index]) }
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
                onBackClick = {}
            )
        }
    }
}