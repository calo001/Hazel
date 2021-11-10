package com.github.calo001.hazel.model.hazeldb

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Animal(
    @SerialName("name")
    val name: String,
    @SerialName("phonetic")
    val phonetic: String,
    @SerialName("emoji_code")
    val emojiCode: String,
)