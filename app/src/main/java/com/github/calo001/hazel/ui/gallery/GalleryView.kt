package com.github.calo001.hazel.ui.gallery

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.github.calo001.hazel.model.unsplash.Result
import com.github.calo001.hazel.ui.common.HazelToolbarSimple
import com.github.calo001.hazel.ui.common.SurfaceToolbar
import com.github.calo001.hazel.ui.common.safeSpacer
import com.github.calo001.hazel.ui.main.calculateItemsPerColumn

@ExperimentalComposeUiApi
@Composable
fun GalleryView(
    title: String,
    onBackClick: () -> Unit,
    unsplashResult: List<Result>
) {
    val itemsPerColumns = calculateItemsPerColumn(
        LocalConfiguration.current.screenWidthDp.dp
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        PhotosContent(
            unsplashResult = unsplashResult,
            itemsPerColumns = itemsPerColumns,
            modifier = Modifier
        )

        SurfaceToolbar {
            HazelToolbarSimple(
                title = title,
                subtitle = "Gallery",
                onBackClick = onBackClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun PhotosContent(
    unsplashResult: List<Result>,
    itemsPerColumns: Int,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        modifier = modifier,
    ) {
        val itemRows = unsplashResult.chunked(itemsPerColumns)
        safeSpacer(20.dp)
        items(itemRows.size) { indexRow ->
            val width = LocalConfiguration.current.screenWidthDp.div(itemsPerColumns).dp - 24.dp
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                itemRows[indexRow].forEach { result ->
                    Column {
                        Card(
                            shape = MaterialTheme.shapes.large,
                            elevation = 16.dp
                        ) {
                            Image(
                                contentScale = ContentScale.Crop,
                                painter = rememberImagePainter(
                                    data = result.urls.small,
                                ),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(width)
                            )
                        }
                        Spacer(modifier = Modifier.size(16.dp))
                    }
                }
                if (itemRows[indexRow].size < itemsPerColumns) {
                    Box(modifier = Modifier.size(width))
                    Spacer(modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}