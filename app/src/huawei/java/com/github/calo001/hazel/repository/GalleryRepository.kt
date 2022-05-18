package com.github.calo001.hazel.repository

import com.github.calo001.hazel.providers.NetworkHelper
import com.github.calo001.hazel.model.network.TokenHw
import com.github.calo001.hazel.providers.SearchKitHelper
import com.huawei.hms.searchkit.SearchKitInstance
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class GalleryRepository (private val networkHelper: NetworkHelper) {
    private var _searchHelper: SearchKitHelper? = null

    fun initSearchHelper(searchHelper: SearchKitHelper) {
        _searchHelper = searchHelper
    }

    fun getImage(queryArgs: String): List<String> {
        val result = getToken()
        return if (result is NetworkResult.Success<*> && result.content is String) {
            val images = _searchHelper?.searchImage(result.content, queryArgs) ?: emptyList()
            images.mapNotNull {
                it.sourceImage.imageContentUrl
            }
        } else {
            emptyList()
        }
    }

    private fun getToken(): NetworkResult {
        val token = networkHelper.getToken()
        val (accessToken, _, _) = Json.decodeFromString<TokenHw>(token)

        SearchKitInstance.getInstance().setInstanceCredential(token)
        return NetworkResult.Success(accessToken)
    }
}