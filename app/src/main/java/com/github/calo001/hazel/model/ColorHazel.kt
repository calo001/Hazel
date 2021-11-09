package com.github.calo001.hazel.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ColorHazel(
    @SerialName("code")
    val code: String, // #FFFFFF
    @SerialName("examples")
    val examples: List<String>,
    @SerialName("name")
    val name: String, // white
    @SerialName("phonetic")
    val phonetic: String, // /waɪt/
    @SerialName("type")
    val type: String // common
)