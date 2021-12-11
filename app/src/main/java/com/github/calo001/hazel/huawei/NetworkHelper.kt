package com.github.calo001.hazel.huawei

import com.github.calo001.hazel.BuildConfig
import com.huawei.hms.network.httpclient.HttpClient
import com.huawei.hms.network.httpclient.Response
import com.huawei.hms.network.restclient.RestClient
import com.huawei.hms.network.httpclient.Submit
import com.huawei.hms.network.restclient.anno.*

import com.orhanobut.logger.Logger
import java.lang.Exception


object NetworkProvider {
    const val TOKEN_URL = "https://oauth-login.cloud.huawei.com/oauth2/v3/token"

    fun createRestClient(): RestClient {
        return RestClient.Builder()
            .httpClient(HttpClient.Builder().build())
            .build()
    }
}

/**
 * Declare a request API.
 */
internal interface TokenService {
    @POST
    @FormUrlEncoded
    fun getToken(
        @Url url: String,
        @Field("grant_type") grantType: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
    ): Submit<String>
}

class NetworkHelper {
    private val service = NetworkProvider.createRestClient().create(TokenService::class.java)

    fun getToken(): String {
        try {
            val response: Response<String> = service.getToken(
                url = NetworkProvider.TOKEN_URL,
                grantType = "client_credentials",
                clientId = BuildConfig.HMS_APP_ID,
                clientSecret = "49d76c205504171e91a97a475a30765551cb9da5c29496b30bc9d011191514d6"
            ).execute()
            // Process the response. The request is successful.
            Logger.i("response code:" + response.code)
            return response.body
        } catch (e: Exception) {
            var errorMsg = "response onFailure : "
            if (e.message != null) {
                errorMsg += e.message
            }
            Logger.w(errorMsg)
            return ""
        }
    }
}