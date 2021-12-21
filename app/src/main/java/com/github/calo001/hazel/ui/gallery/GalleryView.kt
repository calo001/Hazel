package com.github.calo001.hazel.ui.gallery

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.github.calo001.hazel.ui.common.HazelToolbarSimple
import com.github.calo001.hazel.ui.common.SurfaceToolbar
import com.github.calo001.hazel.ui.common.safeSpacer
import com.github.calo001.hazel.ui.main.GalleryStatus
import com.github.calo001.hazel.ui.main.calculateItemsPerColumn
import com.github.calo001.hazel.util.PainterIdentifier

@ExperimentalComposeUiApi
@Composable
fun GalleryView(
    title: String,
    onBackClick: () -> Unit,
    painterIdentifier: PainterIdentifier,
    galleryStatus: GalleryStatus
) {
    when (galleryStatus) {
        is GalleryStatus.Error -> {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Image(painter = painterIdentifier.getPainter(
                    identifier = "openmoji_1f50d"),
                    contentDescription = null,
                    modifier = Modifier.padding(16.dp)
                )
                Text(
                    text = buildAnnotatedString {
                        append("No coincidences for \"")
                        append(
                            AnnotatedString(
                                title,
                                spanStyle = SpanStyle(textDecoration = TextDecoration.Underline)
                            )
                        )
                        append("\"")
                    },
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
        }
        GalleryStatus.Loading -> {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Image(painter = painterIdentifier.getPainter(
                    identifier = "openmoji_1f50d"),
                    contentDescription = null,
                    modifier = Modifier.padding(16.dp)
                )
                Text(
                    text = buildAnnotatedString {
                        append("Searching photos \"")
                        append(
                            AnnotatedString(
                                title,
                                spanStyle = SpanStyle(textDecoration = TextDecoration.Underline)
                            )
                        )
                        append("\"")
                    },
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
        }
        is GalleryStatus.Success -> {
            val itemsPerColumns = calculateItemsPerColumn(
                LocalConfiguration.current.screenWidthDp.dp
            )

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                PhotosContent(
                    galleryStatus = galleryStatus.content, //unsplashResult.content.results,
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
    }
}

@Composable
private fun PhotosContent(
    galleryStatus: List<String>,
    itemsPerColumns: Int,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        modifier = modifier,
    ) {
        val itemRows = galleryStatus.chunked(itemsPerColumns)
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
                                    data = result,
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