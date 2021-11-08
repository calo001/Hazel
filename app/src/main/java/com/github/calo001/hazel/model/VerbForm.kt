package com.github.calo001.hazel.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerbForm(
    @SerialName("examples")
    val examples: List<String>,
    @SerialName("phonetic")
    val phonetic: String, // /biːt/
    @SerialName("verb")
    val verb: String // beat
)