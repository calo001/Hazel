package com.github.calo001.hazel.ui.main

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationDisabled
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.calo001.hazel.R
import com.github.calo001.hazel.config.DarkMode
import com.github.calo001.hazel.providers.WeatherType
import com.github.calo001.hazel.model.hazeldb.HazelContent
import com.github.calo001.hazel.model.status.SpeechStatus
import com.github.calo001.hazel.model.status.WeatherStatus
import com.github.calo001.hazel.model.view.ItemMenuData
import com.github.calo001.hazel.routes.Routes
import com.github.calo001.hazel.ui.common.*
import com.github.calo001.hazel.ui.dialog.AdMainSection
import com.github.calo001.hazel.ui.theme.Lato
import com.github.calo001.hazel.util.PainterIdentifier
import com.github.calo001.hazel.util.TimeText
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.*
import kotlin.concurrent.schedule


@ExperimentalPermissionsApi
@ExperimentalFoundationApi
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
    temperature: WeatherStatus,
    onCheckWeather: () -> Unit,
    speechStatus: SpeechStatus,
    onSpeechClick: () -> Unit,
    clearSpeechResult: () -> Unit,
    onClickTime: () -> Unit,
    timeDay: String,
    time: String,
) {
    var querySearch by rememberSaveable { mutableStateOf("") }

    Box {
        when (status) {
            is HazelContentStatus.Error,
            HazelContentStatus.Loading -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Image(painter = painterResource(
                        id = R.drawable.ic_hazel_logo_ext_dark),
                        contentDescription = null,
                        modifier = Modifier
                            .width(180.dp)
                    )
                }
            }
            is HazelContentStatus.Success -> {
                MainMenu(
                    hazelContent = status.content,
                    temperature = temperature,
                    painterIdentifier = painterIdentifier,
                    onNavigate = onNavigate,
                    onCheckWeather = onCheckWeather,
                    onClickTime = onClickTime,
                    timeDay = timeDay,
                    time = time,
                )
                ExtendedFloatingActionButton(
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.PhotoCamera,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .padding(start = 8.dp)
                        )
                    },
                    text = {
                        Text(
                            text = "Camera tool".uppercase(),
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .padding(end = 8.dp)
                        )
                    },
                    onClick = {
                        onNavigate(Routes.Camera.name)
                    },
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 12.dp,
                        pressedElevation = 24.dp,
                        hoveredElevation = 16.dp,
                        focusedElevation = 16.dp,
                    ),
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                )
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
                                painterIdentifier = painterIdentifier,
                                spacerTop = 180,
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
                        speechStatus = speechStatus,
                        onSpeechClick = onSpeechClick,
                        onTextChangeSpeech = { result ->
                            querySearch = result
                            if (querySearch.isNotEmpty()) {
                                onSearchQuery(result)
                            }
                            clearSpeechResult()
                        },
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
    temperature: WeatherStatus,
    onCheckWeather: () -> Unit,
    onClickTime: () -> Unit,
    timeDay: String,
    time: String,
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

        headerSection(
            onClickTime = onClickTime,
            timeDay = timeDay,
            time = time,
        )

        bigSection(
            title = "The weather",
            temperature = temperature,
            onCheckWeather = onCheckWeather,
            onClick = { onNavigate(Routes.Weather.name) },
        )

        val usefulPhraseCategory = hazelContent.usefulPhrases
        sectionMenu(
            title = "Useful phrases",
            itemsPerColumns = itemsPerColumns,
            items = usefulPhraseCategory.map { usefulPhrases ->
                ItemMenuData(
                    id = usefulPhrases.id,
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

        val seasons = ItemMenuData(
                id = Routes.Seasons.name,
                name = Routes.Seasons.label,
                iconName = "openmoji_1f33b"
            )

        sectionMenu(
            title = "Basic vocabulary",
            itemsPerColumns = itemsPerColumns,
            items = listOf(regularVerb, irregularVerbs, colors, animals, countries, seasons),
            painterIdentifier = painterIdentifier,
            onClick = { id ->
                when (id) {
                    Routes.Colors.name -> onNavigate(Routes.Colors.name)
                    Routes.Countries.name -> onNavigate(Routes.Countries.name)
                    Routes.VerbsRegular.name -> onNavigate(Routes.VerbsRegular.name)
                    Routes.VerbsIrregular.name -> onNavigate(Routes.VerbsIrregular.name)
                    Routes.Animals.name -> onNavigate(Routes.Animals.name)
                    Routes.Seasons.name -> onNavigate(Routes.Seasons.name)
                }
            }
        )

//        item {
//            AdMainSection()
//        }

        item {
            Spacer(modifier = Modifier.size(80.dp))
        }
    }
}


@ExperimentalMaterialApi
fun LazyListScope.bigSection(
    title: String,
    temperature: WeatherStatus,
    onClick: () -> Unit,
    onCheckWeather: () -> Unit,
) {
    item {
        Spacer(modifier = Modifier.size(16.dp))
    }
    item {
        val imageLogoIds = getLogosId(temperature)
        BigCard(
            title = title,
            shapeLabel = {
                WeatherLogo(
                    temperature = temperature,
                    onCheckWeather = onCheckWeather
                )
            },
            imageContent = {
                Box(modifier = Modifier
                    .size(60.dp)
                    .rotate(45f)) {
                    imageLogoIds.forEachIndexed { index, id ->
                        Image(
                            painter = painterResource(id = imageLogoIds[index]),
                            contentDescription = null,
                            modifier = Modifier
                                .size(if (imageLogoIds.size == 1) 60.dp else 50.dp)
                                .align(if (index % 2 == 0) Alignment.TopStart else Alignment.BottomEnd)
                        )
                    }
                }
            },
            onClick = onClick
        )
    }
    item {
        Spacer(modifier = Modifier.size(16.dp))
    }
}

fun getLogosId(temperature: WeatherStatus): List<Int> = when(temperature) {
    WeatherStatus.Error -> listOf(R.drawable.openmoji_1f325)
    WeatherStatus.Loading -> listOf(R.drawable.openmoji_1f325)
    WeatherStatus.LocationFailure -> listOf(R.drawable.openmoji_1f325)
    WeatherStatus.LocationNotGranted -> listOf(R.drawable.openmoji_1f325)
    WeatherStatus.NoAvailable -> listOf(R.drawable.openmoji_1f325)
    is WeatherStatus.Success -> WeatherType.getIdRes(temperature.typeWeather)
}

@Composable
private fun WeatherLogo(
    temperature: WeatherStatus,
    onCheckWeather: () -> Unit
) {
    AnimatedVisibility(visible = temperature is WeatherStatus.LocationFailure) {
        HazelToolbarButton(
            icon = Icons.Filled.LocationOff,
            onClick = onCheckWeather,
            background = MaterialTheme.colors.primary,
            modifier = Modifier
                .size(52.dp)
                .rotate(22f)
        )
    }
    AnimatedVisibility(visible = temperature is WeatherStatus.LocationNotGranted) {
        HazelToolbarButton(
            icon = Icons.Filled.LocationOff,
            onClick = onCheckWeather,
            background = MaterialTheme.colors.primary,
            modifier = Modifier
                .size(52.dp)
                .rotate(22f)
        )
    }
    AnimatedVisibility(visible = temperature is WeatherStatus.Error) {
        HazelToolbarButton(
            icon = Icons.Filled.LocationDisabled,
            onClick = onCheckWeather,
            background = MaterialTheme.colors.primary,
            modifier = Modifier
                .size(52.dp)
                .rotate(22f)
        )
    }
    AnimatedVisibility(visible = temperature is WeatherStatus.Loading) {
        CircularProgressIndicator(
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier
                .size(52.dp)
                .padding(8.dp)
        )
    }
    AnimatedVisibility(visible = temperature is WeatherStatus.Success) {
        Text(
            text = "${
                (temperature as? WeatherStatus.Success)
                    ?.temperature
                    ?: ""
            }º",
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.h4,
            modifier = Modifier
                .rotate(45f)
        )
    }
}

@ExperimentalMaterialApi
fun LazyListScope.headerSection(
    onClickTime: () -> Unit,
    timeDay: String,
    time: String,
) {
    item {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "Good",
                    style = MaterialTheme.typography.h6.copy(
                        fontFamily = Lato
                    ),
                    modifier = Modifier
                )
                Text(
                    text = "$timeDay!",
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier
                )
            }
            Clock(onClickTime, time)
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun Clock(
    onClickTime: () -> Unit,
    time: String
) {
    ItemMenu(
        title = time,
        titleStyle = MaterialTheme.typography.h5,
        spaceText = "",
        image = painterResource(id = R.drawable.openmoji_1f9ed),
        onClick = onClickTime,
        modifier = Modifier
    )
}

@ExperimentalPermissionsApi
@ExperimentalFoundationApi
@Composable
fun SearchResults(
    modifier: Modifier = Modifier,
    querySearch: String,
    searchStatus: SearchStatus,
    onNavigate: (String) -> Unit,
    painterIdentifier: PainterIdentifier,
    spacerTop: Int = 0,
    header: @Composable () -> Unit = {},
) {
    when (searchStatus) {
        SearchStatus.Loading -> { }
        is SearchStatus.Error -> {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier.fillMaxSize()
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
            LazyColumn(modifier) {
                item { Spacer(modifier = Modifier.height(spacerTop.dp)) }
                item { header() }
                items(
                    count = searchStatus.result.size,
                    key = { index -> searchStatus.result[index].route }
                ) { index ->
                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        color = MaterialTheme.colors.background.copy(alpha = 0.8f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .padding(vertical = 2.dp)
                            .animateItemPlacement()
                    ) {
                        SearchItem(
                            searchResult = searchStatus.result[index],
                            onNavigate = onNavigate,
                            painterIdentifier = painterIdentifier
                        )
                    }
                }
                item {
//                    SimpleRoundedBanner(
//                        modifier = Modifier
//                            .padding(16.dp)
//                            .fillMaxWidth()
//                    )
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
fun LazyListScope.sectionMenu(
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