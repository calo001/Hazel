package com.github.calo001.hazel.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.calo001.hazel.model.HazelContent
import com.github.calo001.hazel.model.view.ItemMenuData
import com.github.calo001.hazel.routes.Routes
import com.github.calo001.hazel.util.PainterIdentifier
import com.github.calo001.hazel.ui.common.HazelToolbar
import com.github.calo001.hazel.ui.common.SearchBar
import com.github.calo001.hazel.ui.common.SurfaceToolbar
import com.github.calo001.hazel.ui.common.safeSpacer

@ExperimentalMaterialApi
@Composable
fun MainScreen(
    status: HazelContentStatus,
    painterIdentifier: PainterIdentifier,
    onNavigate: (Routes, String) -> Unit,
) {
    Box {
        when (status) {
            HazelContentStatus.Loading -> {

            }
            is HazelContentStatus.Success -> {
                MainMenu(
                    hazelContent = status.content,
                    painterIdentifier = painterIdentifier,
                    onNavigate = onNavigate
                )
            }
            is HazelContentStatus.Error -> {

            }
        }

        SurfaceToolbar {
            HazelToolbar()
            SearchBar(
                placeholder = "Search what you need",
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }

}

@ExperimentalMaterialApi
@Composable
fun MainMenu(
    hazelContent: HazelContent,
    painterIdentifier: PainterIdentifier,
    onNavigate: (Routes, String) -> Unit,
) {
    val itemsPerColumns = calculateItemsPerColumn(
        LocalConfiguration.current.screenWidthDp.dp
    )

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
    ) {
        safeSpacer(20.dp)

        val usefulPhraseCategory = hazelContent.usefulPhrases
        SectionMenu(
            title = "Useful phrases",
            itemsPerColumns = itemsPerColumns,
            items = usefulPhraseCategory.map { usefulPhrases ->
                ItemMenuData(
                    id = usefulPhrases.category,
                    name = usefulPhrases.category,
                    iconName = usefulPhrases.emojiCode
                ) },
            painterIdentifier = painterIdentifier,
            onClick = { category ->
                onNavigate(
                    Routes.UsefulExpressions,
                    category
                )
            }
        )

        val colors = ItemMenuData(
                id = Routes.Colors.name,
                name = Routes.Colors.label,
                iconName = "openmoji_2b21_fe0f_200d_1f308"
            )

        val irregularVerbs = ItemMenuData(
                id = Routes.VerbsIrregular.name,
                name = Routes.VerbsIrregular.label,
                iconName = "openmoji_1f93e_200d_2640_fe0f"
            )

        val regularVerb = ItemMenuData(
                id = Routes.VerbsRegular.name,
                name = Routes.VerbsRegular.label,
                iconName = "openmoji_1f939_200d_2642_fe0f"
            )

        val countries = ItemMenuData(
                id = Routes.Countries.name,
                name = Routes.Countries.label,
                iconName = "openmoji_1f5fa"
            )

        val animals = ItemMenuData(
                id = Routes.Animals.name,
                name = Routes.Animals.label,
                iconName = "openmoji_1f638"
            )

        SectionMenu(
            title = "Basic vocabulary",
            itemsPerColumns = itemsPerColumns,
            items = listOf(colors, irregularVerbs, regularVerb, countries, animals),
            painterIdentifier = painterIdentifier,
            onClick = { id ->
                when (id) {
                    Routes.Colors.name -> onNavigate(Routes.Colors, "")
                    Routes.Countries.name -> onNavigate(Routes.Countries, "")
                    Routes.VerbsRegular.name -> onNavigate(Routes.VerbsRegular, "")
                    Routes.VerbsIrregular.name -> onNavigate(Routes.VerbsRegular, "")
                    Routes.Animals.name -> onNavigate(Routes.Animals, "")
                }
            }
        )
    }
}

@ExperimentalMaterialApi
fun LazyListScope.SectionMenu(
    title: String,
    items: List<ItemMenuData>,
    itemsPerColumns: Int,
    painterIdentifier: PainterIdentifier,
    onClick: (String) -> Unit
) {
    item {
        Text(
            text = title,
            style = MaterialTheme.typography.h4,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .padding(top = 8.dp)
        )
    }

    val itemRows = items.chunked(itemsPerColumns)
    items(itemRows.size) { indexRow ->
        val width = LocalConfiguration.current.screenWidthDp.div(itemsPerColumns).dp - 24.dp
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            val row = itemRows[indexRow]
            (0 until itemsPerColumns).forEach { itemIndex ->
                val item = row.getOrNull(itemIndex)
                if (item != null) {
                    Column(
                        modifier = Modifier
                            .width(width)
                    ) {
                        ItemMenu(
                            title = row[itemIndex].name,
                            image = painterIdentifier
                                .getPainter(identifier = row[itemIndex].iconName),
                            onClick = {
                                onClick(row[itemIndex].id)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (itemIndex < itemsPerColumns - 1) {
                            Spacer(modifier = Modifier.size(16.dp))
                        }
                    }
                } else {
                    Spacer(
                        modifier = Modifier
                            .width(width)
                    )
                }
            }
        }
    }
}

fun calculateItemsPerColumn(currentWidthScreen: Dp) =
    ((currentWidthScreen.value) / 160f).toInt().let {
        if (it <= 1) {
            2
        } else it
    }

@Preview(device = Devices.PIXEL_4_XL) // Píxel 3 XL
@Composable
fun MainScreenPreview() {
    //MainScreen(HazelContentStatus.Loading, painterIdentifier)
}
//
//val calors = hazelContent.colors.map { color ->
//    ItemMenuData(
//        id = Routes.Colors.name,
//        name = Routes.Colors.label,
//        iconName = "openmoji_2b21_fe0f_200d_1f308"
//    )
//}
//
//val irregularVerbs = hazelContent.irregularVerbs.map {
//    ItemMenuData(
//        id = Routes.VerbsIrregular.name,
//        name = Routes.VerbsIrregular.label,
//        iconName = "openmoji_1f93e_200d_2640_fe0f"
//    )
//}
//
//val regularVerb = hazelContent.regularVerbs.map {
//    ItemMenuData(
//        id = Routes.VerbsRegular.name,
//        name = Routes.VerbsRegular.label,
//        iconName = "openmoji_1f939_200d_2642_fe0f"
//    )
//}
//
//val countries = hazelContent.countries.map {
//    ItemMenuData(
//        id = Routes.Countries.name,
//        name = Routes.Countries.label,
//        iconName = "openmoji_1f5fa"
//    )
//}