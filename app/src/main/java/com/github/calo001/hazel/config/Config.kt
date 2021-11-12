package com.github.calo001.hazel.config

import android.annotation.SuppressLint
import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color
import com.github.calo001.hazel.ui.theme.*

sealed interface ColorVariant {
    val darkColors: Colors
    val lightColors: Colors

    object Green: ColorVariant {
        override val lightColors = lightColors(
            primary = Color.LightGreenA100,
            primaryVariant = Color.LightGreen100,
            onPrimary = Color.Black,
            secondary = Color.LightGreenA100,
            onSecondary = Color.Black,
            background = Color.LightGreen50,
            onBackground = Color.Black,
            surface = Color.GreenA100,
            onSurface = Color.Black,
        )

        @SuppressLint("ConflictingOnColor")
        override val darkColors = darkColors(
            primary = Color.LightGreenA100,
            primaryVariant = Color.LightGreen200,
            onPrimary = Color.Black,
            secondary = Color.LightGreenA100,
            onSecondary = Color.Black,
            background = Color.Gray900,
            onBackground = Color.White,
            surface = Color.GreenA100,
            onSurface = Color.Black,
        )
    }

    object Blue: ColorVariant {
        override val lightColors = lightColors(
            primary = Color.CyanA100,
            primaryVariant = Color.Cyan100,
            onPrimary = Color.Black,
            secondary = Color.CyanA100,
            onSecondary = Color.Black,
            background = Color.Cyan50,
            onBackground = Color.Black,
            surface = Color.LightBlueA100,
            onSurface = Color.Black,
        )

        override val darkColors = darkColors(
            primary = Color.CyanA100,
            primaryVariant = Color.Cyan200,
            onPrimary = Color.Black,
            secondary = Color.CyanA100,
            onSecondary = Color.Black,
            background = Color.Gray900,
            onBackground = Color.White,
            surface = Color.LightBlueA100,
            onSurface = Color.Black,
        )
    }

    object Pink: ColorVariant {
        override val lightColors = lightColors(
            primary = Color.PinkA100,
            primaryVariant = Color.Pink100,
            onPrimary = Color.Black,
            secondary = Color.PinkA100,
            onSecondary = Color.Black,
            background = Color.Pink50,
            onBackground = Color.Black,
            surface = Color.RedA100,
            onSurface = Color.Black,
        )

        override val darkColors = darkColors(
            primary = Color.PinkA100,
            primaryVariant = Color.Pink200,
            onPrimary = Color.Black,
            secondary = Color.PinkA100,
            onSecondary = Color.Black,
            background = Color.Gray900,
            onBackground = Color.White,
            surface = Color.RedA100,
            onSurface = Color.Black,
        )
    }

    object Indigo: ColorVariant {
        override val lightColors = lightColors(
            primary = Color.IndigoA100,
            primaryVariant = Color.Indigo100,
            onPrimary = Color.Black,
            secondary = Color.IndigoA100,
            onSecondary = Color.Black,
            background = Color.Indigo50,
            onBackground = Color.Black,
            surface = Color.BlueA100,
            onSurface = Color.Black,
        )

        override val darkColors = darkColors(
            primary = Color.IndigoA100,
            primaryVariant = Color.Indigo200,
            onPrimary = Color.Black,
            secondary = Color.IndigoA100,
            onSecondary = Color.Black,
            background = Color.Gray900,
            onBackground = Color.White,
            surface = Color.BlueA100,
            onSurface = Color.Black,
        )
    }

    object Amber: ColorVariant {
        override val lightColors = lightColors(
            primary = Color.AmberA100,
            primaryVariant = Color.Amber100,
            onPrimary = Color.Black,
            secondary = Color.AmberA100,
            onSecondary = Color.Black,
            background = Color.Amber50,
            onBackground = Color.Black,
            surface = Color.OrangeA100,
            onSurface = Color.Black,
        )

        override val darkColors = darkColors(
            primary = Color.AmberA100,
            primaryVariant = Color.Amber200,
            onPrimary = Color.Black,
            secondary = Color.AmberA100,
            onSecondary = Color.Black,
            background = Color.Gray900,
            onBackground = Color.White,
            surface = Color.OrangeA100,
            onSurface = Color.Black,
        )
    }

    object Purple: ColorVariant {
        override val lightColors = lightColors(
            primary = Color.PurpleA100,
            primaryVariant = Color.Purple100,
            onPrimary = Color.Black,
            secondary = Color.PurpleA100,
            onSecondary = Color.Black,
            background = Color.Purple50,
            onBackground = Color.Black,
            surface = Color.DeepPurpleA100,
            onSurface = Color.Black,
        )

        override val darkColors = darkColors(
            primary = Color.PurpleA100,
            primaryVariant = Color.Purple200,
            onPrimary = Color.Black,
            secondary = Color.PurpleA100,
            onSecondary = Color.Black,
            background = Color.Gray900,
            onBackground = Color.White,
            surface = Color.DeepPurpleA100,
            onSurface = Color.Black,
        )
    }
}