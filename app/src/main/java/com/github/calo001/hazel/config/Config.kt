package com.github.calo001.hazel.config

import android.annotation.SuppressLint
import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import com.github.calo001.hazel.ui.theme.*

sealed interface ColorVariant {
    val darkColors: Colors
    val lightColors: Colors

    object Green: ColorVariant {
        override val lightColors = lightColors(
            primary = LightGreenA100,
            primaryVariant = LightGreen100,
            onPrimary = Black,
            secondary = LightGreenA100,
            onSecondary = Black,
            background = LightGreen50,
            onBackground = Black,
            surface = GreenA200,
            onSurface = Black,
        )

        @SuppressLint("ConflictingOnColor")
        override val darkColors = darkColors(
            primary = LightGreenA100,
            primaryVariant = LightGreen200,
            onPrimary = Black,
            secondary = LightGreenA100,
            onSecondary = Black,
            background = Gray900,
            onBackground = Black,
            surface = GreenA200,
            onSurface = Black,
        )
    }

    object Blue: ColorVariant {
        override val lightColors = lightColors(
            primary = LightBlueA200,
            primaryVariant = Cyan100,
            onPrimary = Black,
            secondary = Cyan100,
            onSecondary = Black,
            background = Cyan50,
            onBackground = Black,
            surface = LightBlueA200,
            onSurface = Black,
        )

        override val darkColors = darkColors(
            primary = LightBlueA200,
            primaryVariant = Cyan200,
            onPrimary = Black,
            secondary = Cyan100,
            onSecondary = Black,
            background = Gray900,
            onBackground = Black,
            surface = LightBlueA200,
            onSurface = Black,
        )
    }
}