package com.github.calo001.hazel.model.hazeldb

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Weather(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("phonetic")
    val phonetic: String,
    @SerialName("emoji_codes")
    val emojiCodes: List<String>,
)