package com.github.calo001.hazel.model.hazeldb


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Phrase(
    @SerialName("id")
    val id: String,
    @SerialName("expression")
    val expression: String, // Sorry.
    @SerialName("how_to_use")
    val howToUse: String // "Sorry" is a general short apology. We use this when we bump into people on the street. At other times, it sounds too weak.
)