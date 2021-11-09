package com.github.calo001.hazel.network

object UnsplashServiceProvider {
    val service by lazy { UnsplashService(ktorHttpClient) }
}