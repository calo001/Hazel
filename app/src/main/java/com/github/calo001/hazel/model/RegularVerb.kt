package com.github.calo001.hazel.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegularVerb(
    @SerialName("base")
    val base: BaseX,
    @SerialName("emojiCode")
    val emojiCode: String, // 1F91D
    @SerialName("ing")
    val ing: IngX,
    @SerialName("pastParticiple")
    val pastParticiple: PastParticipleX,
    @SerialName("simplePast")
    val simplePast: SimplePastX
)