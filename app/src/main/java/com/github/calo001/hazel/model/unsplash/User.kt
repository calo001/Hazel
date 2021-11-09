package com.github.calo001.hazel.model.unsplash


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("id")
    val id: String, // f4Kr7Nj0mtk
    @SerialName("updated_at")
    val updatedAt: String, // 2021-11-09T12:31:55-05:00
    @SerialName("username")
    val username: String // drew_beamer
)