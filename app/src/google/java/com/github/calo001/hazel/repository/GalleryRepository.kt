package com.github.calo001.hazel.repository

import com.github.calo001.hazel.network.UnsplashServiceProvider
import com.github.calo001.hazel.providers.NetworkHelper
import com.github.calo001.hazel.providers.SearchKitHelper
import com.github.calo001.hazel.ui.main.GalleryStatus

class GalleryRepository (private val networkHelper: NetworkHelper) {
    private val unsplashService = UnsplashServiceProvider.service


    fun getToken(): NetworkResult {
        return NetworkResult.Error(NullPointerException())
    }

    suspend fun getImage(queryArg: String): List<String> {
        val result = unsplashService.search(query = queryArg)
        return result.results.map { it.urls.small }
    }

    fun initSearchHelper(searchKitHelper: SearchKitHelper) {}
}