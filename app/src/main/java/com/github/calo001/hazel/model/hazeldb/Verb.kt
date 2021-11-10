package com.github.calo001.hazel.model.hazeldb


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Verb(
    @SerialName("base")
    val base: VerbForm,
    @SerialName("emojiCode")
    val emojiCode: String, // 1F94A
    @SerialName("ing")
    val ing: Ing,
    @SerialName("pastParticiple")
    val pastParticiple: PastParticiple,
    @SerialName("simplePast")
    val simplePast: SimplePast
)