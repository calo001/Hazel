package com.github.calo001.hazel.providers

import android.content.Context
import com.github.calo001.hazel.BuildConfig
import com.huawei.hms.searchkit.SearchKitInstance
import com.huawei.hms.searchkit.bean.CommonSearchRequest
import com.huawei.hms.searchkit.bean.ImageItem
import com.huawei.hms.searchkit.utils.Language
import com.huawei.hms.searchkit.utils.Region

class SearchKitHelper(context: Context) {
    init {
        SearchKitInstance.init(context, BuildConfig.HMS_APP_ID)
    }

    fun searchImage(token: String, query: String): List<ImageItem> {
        val commonSearchRequest = CommonSearchRequest()
        commonSearchRequest.setQ(query)
        commonSearchRequest.setLang(Language.ENGLISH)
        commonSearchRequest.setSregion(Region.WHOLEWORLD)
        commonSearchRequest.setPs(30)
        commonSearchRequest.setPn(1)

        val imageSearcher = SearchKitInstance.getInstance().imageSearcher.apply {
            setCredential(token)
        }
        val response = imageSearcher.search(commonSearchRequest)
        return response?.data ?: listOf()
    }
}