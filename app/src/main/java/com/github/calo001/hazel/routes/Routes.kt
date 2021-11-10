package com.github.calo001.hazel.routes

sealed class Routes(val name: String, val label: String) {
    object Main: Routes(name = "main", label = "Main")
    object UsefulExpressions: Routes(name = "useful_expressions", label = "Useful expressions")
    object UsefulExpressionsPhrase: Routes(name = "useful_expressions_phrase", label = "Useful expressions")
    object Colors: Routes(name = "color", label = "Colors")
    object Gallery: Routes(name = "gallery", label = "Gallery")
    object Animals: Routes(name = "animals", label = "Animals")
    object VerbsRegular: Routes(name = "verbs/regular", label = "Regular verbs")
    object VerbsIrregular: Routes("verbs/irregular", label = "Irregular verbs")
    object Verbs: Routes("verbs", label = "Irregular verbs")
    object Countries: Routes(name = "countries", label = "Countries")
}