package com.github.calo001.hazel.ui.animals

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.github.calo001.hazel.huawei.TextToSpeechStatus
import com.github.calo001.hazel.model.hazeldb.Animal
import com.github.calo001.hazel.ui.common.HazelToolbarAnimal
import com.github.calo001.hazel.ui.usefulexp.ControlsItem
import com.github.calo001.hazel.util.PainterIdentifier

@ExperimentalMaterialApi
@Composable
fun AnimalContentView(
    animal: Animal,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onListen: () -> Unit,
    onNavBack: () -> Unit,
    onOpenLink: () -> Unit,
    onGallery: () -> Unit,
    onShareClick: () -> Unit,
    hasNext: Boolean,
    hasPrevious: Boolean,
    painterIdentifier: PainterIdentifier,
    textToSpeechStatus: TextToSpeechStatus,
) {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (toolbar, content, controls) = createRefs()
        HazelToolbarAnimal(
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

        TextImageColumn(
            text = animal.name,
            phonetic = animal.phonetic,
            image = painterIdentifier.getPainter(identifier = animal.emojiCode),
            modifier = Modifier.constrainAs(content) {
                centerHorizontallyTo(parent)
                top.linkTo(toolbar.bottom)
                bottom.linkTo(controls.top)
            }
        )

        ControlsItem(
            onPreviousClick = onPrevious,
            onNextClick = onNext,
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