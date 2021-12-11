package com.github.calo001.hazel.model.network


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenHw(
    @SerialName("access_token")
    val accessToken: String, //
    @SerialName("expires_in")
    val expiresIn: Int, //
    @SerialName("token_type")
    val tokenType: String //
)