package com.github.calo001.hazel.ui.common

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.SystemUiController

@Composable
fun SystemBars(
    systemUiController: SystemUiController,
    useDarkIcons: Boolean
) {
    val color = MaterialTheme.colors.background
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = color,
            darkIcons = useDarkIcons
        )

        systemUiController.setStatusBarColor(
            color = color,
            darkIcons = useDarkIcons
        )

        systemUiController.setNavigationBarColor(
            color = color,
            darkIcons = useDarkIcons
        )
    }
}