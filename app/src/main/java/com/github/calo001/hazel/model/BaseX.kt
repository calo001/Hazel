package com.github.calo001.hazel.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseX(
    @SerialName("examples")
    val examples: List<String>,
    @SerialName("phonetic")
    val phonetic: String, // /əkˈsept/
    @SerialName("verb")
    val verb: String // accept
)