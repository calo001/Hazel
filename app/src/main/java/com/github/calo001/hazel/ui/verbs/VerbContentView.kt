package com.github.calo001.hazel.ui.verbs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.github.calo001.hazel.huawei.TextToSpeechStatus
import com.github.calo001.hazel.model.hazeldb.Verb
import com.github.calo001.hazel.ui.animals.TextImageColumn
import com.github.calo001.hazel.ui.common.HazelToolbarVerb
import com.github.calo001.hazel.ui.usefulexp.ControlsItem
import com.github.calo001.hazel.util.PainterIdentifier

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun VerbContentView(
    verb: Verb,
    onListen: (String) -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onNavBack: () -> Unit,
    onOpenLink: (String) -> Unit,
    onSeeExamples: (String) -> Unit,
    hasNext: Boolean,
    hasPrevious: Boolean,
    painterIdentifier: PainterIdentifier,
    selectedForm: String,
    textToSpeechStatus: TextToSpeechStatus,
) {
    var selectedInfoName by rememberSaveable { mutableStateOf(selectedForm) }
    val selectedInfo = when (selectedInfoName) {
        VerbData.BaseForm.name -> VerbData.BaseForm
        VerbData.PastForm.name -> VerbData.PastForm
        VerbData.PastParticipleForm.name -> VerbData.PastParticipleForm
        VerbData.IngForm.name -> VerbData.IngForm
        else -> VerbData.BaseForm
    }
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (toolbar, content, controls) = createRefs()
        HazelToolbarVerb(
            onNavBack = onNavBack,
            onOpenLink = {
                onOpenLink(
                    when (selectedInfo) {
                        VerbData.BaseForm -> verb.base.verb
                        VerbData.PastForm -> verb.simplePast.verb
                        VerbData.PastParticipleForm -> verb.pastParticiple.verb
                        VerbData.IngForm -> verb.ing.verb
                    }
                )
            },
            onSeeExamples = {
                onSeeExamples(selectedInfo.name)
            },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .constrainAs(toolbar) {
                    centerHorizontallyTo(parent)
                    top.linkTo(parent.top)
                }
        )

        VerbInfo(
            verb = verb,
            selectedInfo = selectedInfo,
            onSelectedInfoChange = { selectedInfoName = it.name },
            painterIdentifier = painterIdentifier,
            modifier = Modifier.constrainAs(content) {
                centerHorizontallyTo(parent)
                top.linkTo(toolbar.bottom)
                bottom.linkTo(controls.top)
            }
        )

        ControlsItem(
            onPreviousClick = {
                onPrevious()
            },
            onNextClick = {
                onNext()
            },
            onListenClick = {
                onListen(
                    when(selectedInfo) {
                        VerbData.BaseForm -> verb.base.verb
                        VerbData.PastForm -> verb.simplePast.verb
                        VerbData.PastParticipleForm -> verb.pastParticiple.verb
                        VerbData.IngForm -> verb.ing.verb
                    }
                )
            },
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

@ExperimentalAnimationApi
@Composable
fun VerbInfo(
    verb: Verb,
    modifier: Modifier = Modifier,
    painterIdentifier: PainterIdentifier,
    selectedInfo: VerbData,
    onSelectedInfoChange: (VerbData) -> Unit,
) {
    val menu = listOf(
        VerbData.BaseForm,
        VerbData.PastForm,
        VerbData.PastParticipleForm,
        VerbData.IngForm
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        TabBarVerbs(
            menu = menu,
            selectedInfo = selectedInfo,
            onSelectedInfoChange = onSelectedInfoChange
        )

        when (selectedInfo) {
            VerbData.BaseForm -> VerbFormInfo(
                verb = verb.base.verb,
                phonetic = verb.base.phonetic,
                image = verb.emojiCode,
                painterIdentifier = painterIdentifier
            )
            VerbData.PastForm -> VerbFormInfo(
                verb = verb.simplePast.verb,
                phonetic = verb.simplePast.phonetic,
                image = verb.emojiCode,
                painterIdentifier = painterIdentifier
            )
            VerbData.PastParticipleForm -> VerbFormInfo(
                verb = verb.pastParticiple.verb,
                phonetic = verb.pastParticiple.phonetic,
                image = verb.emojiCode,
                painterIdentifier = painterIdentifier
            )
            VerbData.IngForm -> VerbFormInfo(
                verb = verb.ing.verb,
                phonetic = verb.ing.phonetic,
                image = verb.emojiCode,
                painterIdentifier = painterIdentifier
            )
        }
    }
}

@Composable
fun VerbFormInfo(
    verb: String,
    phonetic: String,
    image: String,
    painterIdentifier: PainterIdentifier
) {
    TextImageColumn(
        text = verb,
        phonetic = phonetic,
        image = painterIdentifier.getPainter(identifier = image)
    )
}

sealed class VerbData(val name: String) {
    object BaseForm: VerbData("Base")
    object PastForm: VerbData("Past")
    object PastParticipleForm: VerbData("Past participle")
    object IngForm: VerbData("Ing")
}