package com.github.calo001.hazel.model.unsplash


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UnsplashResult(
    @SerialName("results")
    val results: List<Result>,
    @SerialName("total")
    val total: Int, // 10000
    @SerialName("total_pages")
    val totalPages: Int // 1000
)