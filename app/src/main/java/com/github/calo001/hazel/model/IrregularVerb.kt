package com.github.calo001.hazel.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IrregularVerb(
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