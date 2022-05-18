package com.github.calo001.hazel.ui.ads

import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun SimpleBanner(
    modifier: Modifier = Modifier,
) {
//    val adParam = remember { AdParam.Builder().build() }
//    AndroidView(
//        factory = { context ->
//            BannerView(context).apply {
//                setWidthMatchParent()
//                adId = "testw6vs28auh3"
//                bannerAdSize = BannerAdSize.BANNER_SIZE_SMART
//                loadAd(adParam)
//            }
//        },
//        modifier = modifier
//    )
}

@Composable
fun SimpleRoundedBanner(
    modifier: Modifier = Modifier,
) {
//    val adParam = remember { AdParam.Builder().build() }
//    Card(
//        backgroundColor = Color.Transparent,
//        elevation = 0.dp,
//        modifier = modifier
//    ) {
//        AndroidView(
//            factory = { context ->
//                BannerView(context).apply {
//                    setWidthMatchParent()
//                    adId = "testw6vs28auh3"
//                    bannerAdSize = BannerAdSize.BANNER_SIZE_SMART
//                    loadAd(adParam)
//                } },
//            modifier = Modifier
//        )
//    }
}

@Composable
fun SimpleItemBanner(
    modifier: Modifier = Modifier,
) {
//    val adParam = remember { AdParam.Builder().build() }
//    Card(
//        backgroundColor = Color.Transparent,
//        elevation = 0.dp,
//        modifier = modifier
//    ) {
//        AndroidView(
//            factory = { context ->
//                BannerView(context).apply {
//                    adId = "testw6vs28auh3"
//                    bannerAdSize = BannerAdSize.BANNER_SIZE_300_250
//                    loadAd(adParam)
//                } },
//            modifier = Modifier
//        )
//    }
}