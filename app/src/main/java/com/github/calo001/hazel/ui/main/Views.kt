package com.github.calo001.hazel.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.calo001.hazel.R
import com.github.calo001.hazel.config.ColorVariant
import com.github.calo001.hazel.ui.theme.HazelTheme

@ExperimentalMaterialApi
@Composable
fun ItemMenu(
    modifier: Modifier = Modifier,
    title: String,
    titleStyle: TextStyle =  MaterialTheme.typography.h6,
    spaceText: String = "\n",
    image: Painter,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier,
        onClick = onClick
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(12.dp)
        ) {
            Image(
                painter = image,
                contentDescription = title
            )
            Text(
                text = title + spaceText,
                style = titleStyle,
                maxLines = 2,
                textAlign= TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
fun ItemMenuPreview() {
    HazelTheme(
        colorVariant = ColorVariant.Green
    ) {
        val painter = painterResource(id = R.drawable.ic_launcher_foreground)
        ItemMenu(
            title = "Apologizing",
            image = painter,
            onClick = {}
        )
    }
}