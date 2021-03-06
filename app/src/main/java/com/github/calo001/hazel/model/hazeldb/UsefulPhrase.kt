package com.github.calo001.hazel.model.hazeldb


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UsefulPhrase(
    @SerialName("id")
    val id: String,
    @SerialName("category")
    val category: String, // Apologising
    @SerialName("emoji_code")
    val emojiCode: String, // 1F647
    @SerialName("phrases")
    val phrases: List<Phrase>
)