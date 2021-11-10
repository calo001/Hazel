package com.github.calo001.hazel.model.hazeldb


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Ing(
    @SerialName("examples")
    val examples: List<String>,
    @SerialName("phonetic")
    val phonetic: String, // /ˈbiː.t̬ɪŋ/
    @SerialName("verb")
    val verb: String // beating
)