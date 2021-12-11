package com.github.calo001.hazel.repository

import com.github.calo001.hazel.huawei.NetworkHelper
import com.github.calo001.hazel.model.network.TokenHw
import com.huawei.hms.searchkit.SearchKitInstance
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class GalleryRepository (private val networkHelper: NetworkHelper) {
    fun getToken(): NetworkResult {
        val token = networkHelper.getToken()
        val (accessToken, _, _) = Json.decodeFromString<TokenHw>(token)

        SearchKitInstance.getInstance().setInstanceCredential(token)
        return NetworkResult.Success(accessToken)
    }
}