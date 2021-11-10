package com.github.calo001.hazel.model.hazeldb


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HazelContent(
    @SerialName("colors")
    val colorHazels: List<ColorHazel>,
    @SerialName("countries")
    val countries: List<Country>,
    @SerialName("irregular_verbs")
    val irregularVerbs: List<Verb>,
    @SerialName("regular_verbs")
    val regularVerbs: List<Verb>,
    @SerialName("useful_phrases")
    val usefulPhrases: List<UsefulPhrase>,
    @SerialName("animals")
    val animals: List<Animal>,
)