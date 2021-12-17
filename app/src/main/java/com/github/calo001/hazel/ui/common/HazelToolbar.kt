package com.github.calo001.hazel.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BadgedBox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.calo001.hazel.R
import com.github.calo001.hazel.config.ColorVariant
import com.github.calo001.hazel.config.DarkMode
import com.github.calo001.hazel.ui.theme.HazelTheme

@Composable
fun HazelToolbar(
    onSettingsClick: () -> Unit,
    darkMode: DarkMode,
    onDarkModeChange: (DarkMode) -> Unit,
    showBadge: Boolean = false,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(id =
                when(darkMode) {
                    DarkMode.Dark -> R.drawable.ic_hazel_logo_ext_light
                    DarkMode.Light -> R.drawable.ic_hazel_logo_ext_dark
                    DarkMode.FollowSystem -> if (isSystemInDarkTheme()) {
                        R.drawable.ic_hazel_logo_ext_light
                    } else { R.drawable.ic_hazel_logo_ext_dark }
                }
            ),
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
                onClick = {
                    if (darkMode is DarkMode.Light || darkMode is DarkMode.FollowSystem) {
                        onDarkModeChange(DarkMode.Dark)
                    } else if (darkMode is DarkMode.Dark || darkMode is DarkMode.FollowSystem) {
                        onDarkModeChange(DarkMode.Light)
                    }
                },
                modifier = Modifier
                    .padding(vertical = 8.dp)
            )

            Box() {
                HazelToolbarButton(
                    icon = Icons.Filled.Settings,
                    onClick = onSettingsClick,
                    modifier = Modifier.padding(8.dp)
                )
                if(showBadge) {
                    Box(modifier = Modifier
                        .padding(12.dp)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colors.background,
                            shape = CircleShape
                        )
                        .size(22.dp)
                        .background(
                            color = MaterialTheme.colors.surface,
                            shape = CircleShape
                        )
                        .align(Alignment.TopEnd)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun HazelToolbarPreview() {
    HazelTheme(colorVariant = ColorVariant.Green) {
        HazelToolbar(
            onSettingsClick = {},
            onDarkModeChange = {},
            darkMode = DarkMode.Light,
        )
    }
}