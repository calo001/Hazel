package com.github.calo001.hazel.ui.countries

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.github.calo001.hazel.model.hazeldb.Country
import com.github.calo001.hazel.ui.common.HazelToolbarCountry
import com.github.calo001.hazel.ui.usefulexp.ControlsItem
import com.github.calo001.hazel.util.PainterIdentifier

@ExperimentalMaterialApi
@Composable
fun CountryContentView(
    country: Country,
    onListen: (String) -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onNavBack: () -> Unit,
    onOpenMap: () -> Unit,
    onGallery: () -> Unit,
    hasNext: Boolean,
    hasPrevious: Boolean,
    painterIdentifier: PainterIdentifier,
) {
    var selectedInfo by remember { mutableStateOf<CountryData>(CountryData.Name) }
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (toolbar, content, controls) = createRefs()
        HazelToolbarCountry(
            onNavBack = onNavBack,
            onOpenMap = onOpenMap,
            onGallery = onGallery,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .constrainAs(toolbar) {
                    centerHorizontallyTo(parent)
                    top.linkTo(parent.top)
                }
        )

        CountryInfo(
            country = country,
            selectedInfo = selectedInfo,
            onSelectedInfoChange = { selectedInfo = it },
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
                selectedInfo = CountryData.Name
            },
            onNextClick = {
                onNext()
                selectedInfo = CountryData.Name
            },
            onListenClick = {
                onListen(
                    when(selectedInfo) {
                        CountryData.Language -> country.language
                        CountryData.Name -> country.name
                        CountryData.Nationality -> country.nationality
                    }
                )
            },
            hideNext = !hasNext,
            hidePrevious = !hasPrevious,
            modifier = Modifier.constrainAs(controls) {
                centerHorizontallyTo(parent)
                bottom.linkTo(parent.bottom)
            }
        )
    }
}