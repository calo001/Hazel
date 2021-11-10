package com.github.calo001.hazel.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun HazelToolbar() {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier.fillMaxWidth()
    ) {
        HazelToolbarButton(
            icon = Icons.Filled.DarkMode,
            onClick = {},
            modifier = Modifier
                .padding(vertical = 8.dp)
        )

        HazelToolbarButton(
            icon = Icons.Filled.Settings,
            onClick = {},
            modifier = Modifier.padding(8.dp)
        )
    }
}