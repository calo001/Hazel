package com.github.calo001.hazel.model.unsplash


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Result(
    @SerialName("alt_description")
    val altDescription: String?, // null
    @SerialName("blur_hash")
    val blurHash: String?, // LEP??rIU00M_M{-;ofofD%xut7ay
    @SerialName("color")
    val color: String, // #d9d9d9
    @SerialName("description")
    val description: String?, // Looking for the abstract around the house during the quarantine.
    @SerialName("height")
    val height: Int, // 2830
    @SerialName("id")
    val id: String, // kUHfMW8awpE
    @SerialName("urls")
    val urls: Urls,
    @SerialName("user")
    val user: User,
    @SerialName("width")
    val width: Int // 4244
)