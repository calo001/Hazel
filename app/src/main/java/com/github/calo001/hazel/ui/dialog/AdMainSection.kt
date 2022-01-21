package com.github.calo001.hazel.ui.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.calo001.hazel.ui.ads.SimpleItemBanner

@Composable
fun AdMainSection(itemsPerColumns: Int) {
    Column {
        Text(
            text = "Ads",
            style = MaterialTheme.typography.h4,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .padding(top = 8.dp)
        )
        Row {
            (0 until itemsPerColumns).forEachIndexed { index, _ ->
                SimpleItemBanner(
                    modifier = Modifier.weight(1f)
                )
                if (index < itemsPerColumns - 1) {
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
        }
    }
}