package com.github.calo001.hazel.ui.seasons

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.LifecycleOwner
import com.github.calo001.hazel.providers.PanoramaHelper

@Composable
fun PanoramaView(
    modifier: Modifier = Modifier,
    drawableId: Int,
    panoramaHelper: PanoramaHelper,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
) {
    Image(
        painter = painterResource(id = drawableId),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
    )
}