package com.github.calo001.hazel.repository

import com.github.calo001.hazel.model.unsplash.UnsplashResult
import com.github.calo001.hazel.network.UnsplashService

class UnsplashRepository(private val unsplashService: UnsplashService) {
    suspend fun search(query: String): NetworkResult {
        return try {
            NetworkResult.Success(
                unsplashService.search(query = query)
            )
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }
}

sealed interface NetworkResult {
    object Loading: NetworkResult
    class Success(val unsplashResult: UnsplashResult): NetworkResult
    class Error(val error: Exception): NetworkResult
}