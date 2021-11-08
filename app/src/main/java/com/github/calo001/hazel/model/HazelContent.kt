package com.github.calo001.hazel.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HazelContent(
    @SerialName("colors")
    val colors: List<Color>,
    @SerialName("countries")
    val countries: List<Country>,
    @SerialName("irregular_verbs")
    val irregularVerbs: List<IrregularVerb>,
    @SerialName("regular_verbs")
    val regularVerbs: List<RegularVerb>,
    @SerialName("useful_phrases")
    val usefulPhrases: List<UsefulPhrase>
)