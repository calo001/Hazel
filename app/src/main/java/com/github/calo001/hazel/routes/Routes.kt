package com.github.calo001.hazel.routes

import com.github.calo001.hazel.R

sealed class Routes(val name: String, val label: String, val icon: Int = 0) {
    object Main: Routes(name = "main", label = "Main")
    object Settings: Routes(name = "settins", label = "Countries")
    object UsefulExpressions: Routes(name = "useful_expressions", label = "Useful expressions")
    object UsefulExpressionsPhrase: Routes(name = "useful_expressions_phrase", label = "Useful expressions")
    object Colors: Routes(name = "color", label = "Colors", icon = R.drawable.outline_palette_24)
    object Gallery: Routes(name = "gallery", label = "Gallery")
    object Animals: Routes(name = "animals", label = "Animals", icon = R.drawable.outline_pets_24)
    object VerbsRegular: Routes(name = "verbs/regular", label = "Regular verbs", icon = R.drawable.outline_hdr_weak_24)
    object VerbsIrregular: Routes("verbs/irregular", label = "Irregular verbs", icon = R.drawable.outline_hdr_strong_24)
    object Verbs: Routes("verbs", label = "verbs")
    object Countries: Routes(name = "countries", label = "Countries", icon = R.drawable.outline_public_24)
    object Weather: Routes(name = "weather", label = "The weather", icon = R.drawable.outline_public_24)
    object Seasons: Routes(name = "seasons", label = "Seasons", icon = R.drawable.outline_public_24)
    object Map: Routes(name = "map", label = "Map", icon = R.drawable.outline_public_24)
    object Time: Routes(name = "time", label = "Time", icon = R.drawable.outline_public_24)
    object Camera: Routes(name = "camera", label = "Camera", icon = R.drawable.outline_public_24)
}