package com.github.calo001.hazel.ui.seasons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.calo001.hazel.ui.theme.Lato
import com.huawei.hms.panorama.PanoramaInterface

@Composable
fun ItemSeason(
    modifier: Modifier = Modifier,
    text: String,
    phonetic: String,
    panoramaId: Int,
    panorama: PanoramaInterface.PanoramaLocalInterface,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Surface(
            shape = CircleShape,
            border = BorderStroke(
                width = 4.dp,
                color = Color.Black,
            ),
            modifier = Modifier.size(240.dp),
        ) {
            PanoramaView(
                drawableId = panoramaId,
                panorama = panorama,
            )
        }

        Text(
            text = text,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = phonetic,
            style = LocalTextStyle.current.copy(
                fontFamily = Lato
            ),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}