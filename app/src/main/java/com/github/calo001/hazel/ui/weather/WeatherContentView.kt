package com.github.calo001.hazel.ui.weather

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.github.calo001.hazel.huawei.TextToSpeechStatus
import com.github.calo001.hazel.model.hazeldb.Weather
import com.github.calo001.hazel.ui.common.HazelToolbarWeather
import com.github.calo001.hazel.ui.theme.Lato
import com.github.calo001.hazel.ui.usefulexp.ControlsItem
import com.github.calo001.hazel.util.PainterIdentifier

@ExperimentalMaterialApi
@Composable
fun WeatherContentView(
    weather: Weather,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onListen: () -> Unit,
    onNavBack: () -> Unit,
    hasNext: Boolean,
    hasPrevious: Boolean,
    textToSpeechStatus: TextToSpeechStatus,
    painterIdentifier: PainterIdentifier,
) {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (toolbar, content, controls) = createRefs()
        HazelToolbarWeather(
            onNavBack = onNavBack,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .constrainAs(toolbar) {
                    centerHorizontallyTo(parent)
                    top.linkTo(parent.top)
                }
        )

        ItemWeather(
            text = weather.name,
            phonetic = weather.phonetic,
            images = weather.emojiCodes.map { id ->
                painterIdentifier.getPainter(identifier = id)
            },
            modifier = Modifier.constrainAs(content) {
                centerHorizontallyTo(parent)
                top.linkTo(toolbar.bottom)
                bottom.linkTo(controls.top)
            }
        )

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

@Composable
fun ItemWeather(
    modifier: Modifier = Modifier,
    text: String,
    phonetic: String,
    images: List<Painter>,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(240.dp)
        ) {
            images.forEachIndexed { index, painter ->
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp)
                        .align(
                            when {
                                images.size == 1 -> Alignment.Center
                                index % 2 == 0 -> Alignment.TopStart
                                else -> Alignment.BottomEnd
                            }
                        )
                )
            }
        }

        Text(
            text = text,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = phonetic,
            style = LocalTextStyle.current.copy(
                fontFamily = Lato
            ),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
