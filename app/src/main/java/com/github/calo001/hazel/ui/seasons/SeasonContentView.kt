package com.github.calo001.hazel.ui.seasons

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.github.calo001.hazel.R
import com.github.calo001.hazel.model.hazeldb.Season
import com.github.calo001.hazel.model.status.TextToSpeechStatus
import com.github.calo001.hazel.providers.PanoramaHelper
import com.github.calo001.hazel.ui.common.HazelToolbarSeasons
import com.github.calo001.hazel.ui.usefulexp.ControlsItem

@ExperimentalMaterialApi
@Composable
fun SeasonContentView(
    season: Season,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onListen: () -> Unit,
    onNavBack: () -> Unit,
    onOpenLink: () -> Unit,
    onGallery: () -> Unit,
    onShareClick: () -> Unit,
    onPanoramaClick: () -> Unit,
    hasNext: Boolean,
    hasPrevious: Boolean,
    panorama: PanoramaHelper,
    textToSpeechStatus: TextToSpeechStatus,
) {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (toolbar, content, onTopContent, controls) = createRefs()
        HazelToolbarSeasons(
            onNavBack = onNavBack,
            onOpenLink = onOpenLink,
            onGallery = onGallery,
            onShareClick = onShareClick,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .constrainAs(toolbar) {
                    centerHorizontallyTo(parent)
                    top.linkTo(parent.top)
                }
        )

        ItemSeason(
            text = season.name,
            phonetic = season.seasonPhonetic,
            panoramaId = when(season.id) {
                "seasons_1" -> R.drawable.winter_panorama
                "seasons_2" -> R.drawable.spring_panoramic
                "seasons_3" -> R.drawable.autumn_panoramic
                "seasons_4" -> R.drawable.summer_panoramic
                else -> 0
            },
            panorama = panorama,
            modifier = Modifier.constrainAs(content) {
                centerHorizontallyTo(parent)
                top.linkTo(toolbar.bottom)
                bottom.linkTo(controls.top)
            }
        )

        FloatingActionButton(
            onClick = onPanoramaClick,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .border(
                    width = 2.dp,
                    shape = MaterialTheme.shapes.medium,
                    color = Color.Black
                )
                .constrainAs(onTopContent) {
                    top.linkTo(content.top)
                    end.linkTo(content.end)
                }
        ) {
            Icon(
                imageVector = Icons.Filled.Fullscreen,
                contentDescription = null
            )
        }

        ControlsItem(
            onPreviousClick = onPreviousClick,
            onNextClick = onNextClick,
            onListenClick = onListen,
            hideNext = !hasNext,
            hidePrevious = !hasPrevious,
            textToSpeechStatus = textToSpeechStatus,
            modifier = Modifier.constrainAs(controls) {
                centerHorizontallyTo(parent)
                bottom.linkTo(parent.bottom)
            }
        )
    }
}