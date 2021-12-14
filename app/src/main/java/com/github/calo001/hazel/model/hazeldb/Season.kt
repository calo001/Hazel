package com.github.calo001.hazel.model.hazeldb

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Season(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("season_phonetic")
    val seasonPhonetic: String,
    @SerialName("emoji_code")
    val emojiCode: String,
)