package com.github.calo001.hazel.network

import com.github.calo001.hazel.model.unsplash.UnsplashResult
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.get
import io.ktor.client.request.*

private const val API_URL = "https://api.unsplash.com/search/photos" +
        "?client_id=hSmIdeCRk2-ZHVLSCV-eAnrF4neSbnaLna5AjOUlzuE" +
        "&per_page=30" +
        "&query="

class UnsplashService(private val client: HttpClient) {
    suspend fun search(query: String): UnsplashResult {
        return client.get("$API_URL$query")
    }
}