package com.github.calo001.hazel.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.github.calo001.hazel.R

@Composable
fun HazelToolbar(
    onSettingsClick: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_hazel_logo_ext),
            contentDescription = null,
            modifier = Modifier
                .height(50.dp)
                .padding(start = 16.dp)
        )
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
                onClick = onSettingsClick,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}