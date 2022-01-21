package com.github.calo001.hazel.ui.ads

import androidx.compose.foundation.layout.height
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.calo001.hazel.util.setWidthMatchParent
import com.huawei.hms.ads.AdParam
import com.huawei.hms.ads.BannerAdSize
import com.huawei.hms.ads.banner.BannerView

@Composable
fun SimpleBanner(
    modifier: Modifier = Modifier,
) {
    val adParam = remember { AdParam.Builder().build() }
    AndroidView(
        factory = { context ->
            BannerView(context).apply {
                setWidthMatchParent()
                adId = "testw6vs28auh3"
                bannerAdSize = BannerAdSize.BANNER_SIZE_SMART
                loadAd(adParam)
            }
        },
        modifier = modifier
    )
}

@Composable
fun SimpleRoundedBanner(
    modifier: Modifier = Modifier,
) {
    val adParam = remember { AdParam.Builder().build() }
    Card(
        shape = MaterialTheme.shapes.medium,
        elevation = 12.dp,
        modifier = modifier
    ) {
        AndroidView(
            factory = { context ->
                BannerView(context).apply {
                    setWidthMatchParent()
                    adId = "testw6vs28auh3"
                    bannerAdSize = BannerAdSize.BANNER_SIZE_SMART
                    loadAd(adParam)
                } },
            modifier = Modifier
        )
    }
}

@Composable
fun SimpleItemBanner(
    modifier: Modifier = Modifier,
) {
    val adParam = remember { AdParam.Builder().build() }
    Card(
        modifier = modifier
    ) {
        AndroidView(
            factory = { context ->
                BannerView(context).apply {
                    adId = "testw6vs28auh3"
                    bannerAdSize = BannerAdSize.BANNER_SIZE_300_250
                    loadAd(adParam)
                } },
            modifier = Modifier.height(150.dp)
        )
    }
}