package com.github.calo001.hazel.ui.main

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.calo001.hazel.config.DarkMode
import com.github.calo001.hazel.model.hazeldb.HazelContent
import com.github.calo001.hazel.model.view.ItemMenuData
import com.github.calo001.hazel.routes.Routes
import com.github.calo001.hazel.ui.common.*
import com.github.calo001.hazel.util.PainterIdentifier
import com.github.calo001.hazel.ui.theme.Lato

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun MainScreen(
    status: HazelContentStatus,
    painterIdentifier: PainterIdentifier,
    onNavigate: (String) -> Unit,
    onSettingsClick: () -> Unit,
    searchStatus: SearchStatus,
    onSearchQuery: (String) -> Unit,
    darkMode: DarkMode,
    onDarkModeChange: (DarkMode) -> Unit,
) {
    var querySearch by rememberSaveable { mutableStateOf("") }

    Box {
        when (status) {
            HazelContentStatus.Loading -> {

            }
            is HazelContentStatus.Success -> {
                MainMenu(
                    hazelContent = status.content,
                    painterIdentifier = painterIdentifier,
                    onNavigate = onNavigate,
                )
            }
            is HazelContentStatus.Error -> {

            }
        }

        SurfaceToolbar {
            Box(modifier = Modifier.fillMaxWidth()) {
                AnimatedVisibility(
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically(),
                    visible = querySearch.isNotEmpty()
                ) {
                    Box {
                        Column(modifier = Modifier
                            .fillMaxSize()
                            .clickable { }
                        ) {
                            SearchResults(
                                querySearch = querySearch,
                                searchStatus = searchStatus,
                                onNavigate = onNavigate,
                                painterIdentifier = painterIdentifier
                            )
                        }
                        Box(
                            modifier = Modifier
                                .background(
                                    brush = Brush.verticalGradient(
                                        (0f to MaterialTheme.colors.background),
                                        (0.8f to MaterialTheme.colors.background.copy(alpha = 0.8f)),
                                        (1f to MaterialTheme.colors.background.copy(alpha = 0f)),
                                    )
                                )
                                .fillMaxWidth()
                                .height(180.dp)
                        )
                    }
                }
                Column {
                    HazelToolbar(
                        darkMode = darkMode,
                        onSettingsClick = onSettingsClick,
                        onDarkModeChange = onDarkModeChange,
                    )
                    SearchBar(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        placeholder = "Search what you need",
                        onTextChange = { query ->
                            querySearch = query
                            if (querySearch.isNotEmpty()) {
                                onSearchQuery(query)
                            }
                        },
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                }
            }
        }
    }

}

@ExperimentalMaterialApi
@Composable
fun MainMenu(
    hazelContent: HazelContent,
    painterIdentifier: PainterIdentifier,
    onNavigate: (String) -> Unit,
) {
    val itemsPerColumns = calculateItemsPerColumn(
        LocalConfiguration.current.screenWidthDp.dp
    )

    val state = rememberLazyListState()

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        state = state
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
                    "${Routes.UsefulExpressions.name}/$category"
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
                    Routes.Colors.name -> onNavigate(Routes.Colors.name)
                    Routes.Countries.name -> onNavigate(Routes.Countries.name)
                    Routes.VerbsRegular.name -> onNavigate(Routes.VerbsRegular.name)
                    Routes.VerbsIrregular.name -> onNavigate(Routes.VerbsIrregular.name)
                    Routes.Animals.name -> onNavigate(Routes.Animals.name)
                }
            }
        )
    }
}

@Composable
fun SearchResults(
    querySearch: String,
    searchStatus: SearchStatus,
    onNavigate: (String) -> Unit,
    painterIdentifier: PainterIdentifier
) {
    when (searchStatus) {
        SearchStatus.Loading -> { }
        is SearchStatus.Error -> {
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
                                querySearch,
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
        is SearchStatus.Success -> {
            LazyColumn{
                item { Spacer(modifier = Modifier.height(180.dp)) }
                items(searchStatus.result.size) { index ->
                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        color = MaterialTheme.colors.background.copy(alpha = 0.8f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .padding(vertical = 2.dp)
                    ) {
                        SearchItem(
                            searchResult = searchStatus.result[index],
                            onNavigate = onNavigate,
                            painterIdentifier = painterIdentifier
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchItem(
    searchResult: SearchResult,
    onNavigate: (String) -> Unit,
    painterIdentifier: PainterIdentifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onNavigate(searchResult.route) }
    ) {
        if (searchResult.color != null) {
            CircleColor(
                color = searchResult.color,
                modifier = Modifier
                    .size(50.dp)
                    .padding(vertical = 4.dp)
                    .padding(horizontal = 8.dp)
            )
        }
        if (searchResult.imageCode != null) {
            Image(
                painter = painterIdentifier.getPainter(identifier = searchResult.imageCode),
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .padding(vertical = 4.dp)
                    .padding(horizontal = 8.dp)
            )
        }

        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            if (searchResult.path.isNotEmpty()) {
                Text(
                    text = searchResult.path,
                    style = MaterialTheme.typography.caption.copy(
                        fontFamily = Lato
                    )
                )
            }

            Text(
                text = searchResult.itemName,
                style = MaterialTheme.typography.h5
            )
        }
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