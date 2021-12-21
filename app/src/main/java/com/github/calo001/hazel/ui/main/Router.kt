package com.github.calo001.hazel.ui.main

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.github.calo001.hazel.config.ColorVariant
import com.github.calo001.hazel.config.DarkMode
import com.github.calo001.hazel.huawei.*
import com.github.calo001.hazel.model.hazeldb.Country
import com.github.calo001.hazel.model.hazeldb.Phrase
import com.github.calo001.hazel.model.hazeldb.Season
import com.github.calo001.hazel.routes.Routes
import com.github.calo001.hazel.ui.animals.AnimalContentView
import com.github.calo001.hazel.ui.animals.AnimalsView
import com.github.calo001.hazel.ui.camera.CameraFeature
import com.github.calo001.hazel.ui.camera.CameraToolView
import com.github.calo001.hazel.ui.camera.ResultCameraView
import com.github.calo001.hazel.ui.colors.ColorsView
import com.github.calo001.hazel.ui.colors.OneColorView
import com.github.calo001.hazel.ui.colors.SimpleExamplesView
import com.github.calo001.hazel.ui.countries.CountryContentView
import com.github.calo001.hazel.ui.countries.CountryView
import com.github.calo001.hazel.ui.gallery.GalleryView
import com.github.calo001.hazel.ui.seasons.SeasonContentView
import com.github.calo001.hazel.ui.seasons.SeasonsView
import com.github.calo001.hazel.ui.settings.Dictionaries
import com.github.calo001.hazel.ui.settings.SettingsView
import com.github.calo001.hazel.ui.time.TimeView
import com.github.calo001.hazel.ui.usefulexp.PhraseView
import com.github.calo001.hazel.ui.usefulexp.UsefulExpressionsView
import com.github.calo001.hazel.ui.verbs.VerbContentView
import com.github.calo001.hazel.ui.verbs.VerbData
import com.github.calo001.hazel.ui.verbs.VerbsView
import com.github.calo001.hazel.ui.weather.WeatherContentView
import com.github.calo001.hazel.ui.weather.WeatherView
import com.github.calo001.hazel.util.PainterIdentifier
import com.github.calo001.hazel.util.TimeText
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.huawei.hms.panorama.PanoramaInterface
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.*
import kotlin.concurrent.schedule

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("MissingPermission")

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun Router(
    navController: NavHostController,
    hazelContentStatus: HazelContentStatus,
    painterIdentifier: PainterIdentifier,
    onListenClick: (String) -> Unit,
    viewModel: MainViewModel,
    onOpenLink: (String) -> Unit,
    onOpenMaps: (Country) -> Unit,
    onSelectColorScheme: (ColorVariant) -> Unit,
    onSelectDictionary: (Dictionaries) -> Unit,
    onSelectDarkMode: (DarkMode) -> Unit,
    darkMode: DarkMode,
    colorScheme: ColorVariant,
    dictionary: Dictionaries,
    defaultRoute: String,
    weatherStatus: WeatherStatus,
    onRequestWeather: () -> Unit,
    onSpeechClick: () -> Unit,
    speechStatus: SpeechStatus,
    panoramaInterface: PanoramaInterface.PanoramaLocalInterface,
    onPanoramaClick: (Season) -> Unit,
    textToSpeechStatus: TextToSpeechStatus,
    onAnalysisCapture: (Bitmap, CameraFeature) -> Unit,
    onCopy: (String) -> Unit,
    showQRDialog: (String) -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = defaultRoute
    ) {
        composable(
            route = "${Routes.Verbs.name}/{type}/{verb_id}/{form}",
            arguments = listOf(
                navArgument("type") { type = NavType.StringType },
                navArgument("verb_id") { type = NavType.StringType },
                navArgument("form") { type = NavType.StringType },
            )
        ) { navBackStackEntry ->
            var verbIdArg by rememberSaveable {
                mutableStateOf(navBackStackEntry.arguments?.getString("verb_id") ?: "")
            }

            val type = navBackStackEntry.arguments?.getString("type") ?: ""
            val form = navBackStackEntry.arguments?.getString("form") ?: VerbData.BaseForm.name

            val verbs = when (type) {
                "regular" -> viewModel.getRegularVerbs()
                "irregular" -> viewModel.getIrregularVerbs()
                else -> listOf()
            }

            val currentVerb = verbs.firstOrNull { it.id == verbIdArg }

            if (currentVerb != null) {
                val indexCurrent = verbs.indexOfFirst { it.id == verbIdArg }
                VerbContentView(
                    verb = currentVerb,
                    selectedForm = form,
                    onNext = {
                        verbIdArg = verbs.getOrElse(indexCurrent + 1) { verbs[indexCurrent] }.id
                    },
                    onPrevious = {
                        verbIdArg = verbs.getOrElse(indexCurrent - 1) { verbs[indexCurrent] }.id
                    },
                    onOpenLink = { term ->
                        onOpenLink(term)
                    },
                    onSeeExamples = {
                        navController.navigate("${when(type) {
                                "regular" -> Routes.VerbsRegular.name
                                "irregular" -> Routes.VerbsIrregular.name
                                else -> Routes.VerbsRegular.name }
                            }/example/${form}/${currentVerb.id}"
                        )
                    },
                    onShareClick = {
                        showQRDialog("${Routes.Verbs.name}/${type}/${currentVerb.id}/${form}")
                    },
                    onListen = { onListenClick(it) },
                    onNavBack = { navController.navigateUp() },
                    hasNext = indexCurrent < verbs.lastIndex,
                    hasPrevious = indexCurrent != 0,
                    painterIdentifier = painterIdentifier,
                    textToSpeechStatus = textToSpeechStatus,
                )
            }
        }

        composable(
            route = "${Routes.Verbs.name}/{type}",
            arguments = listOf(
                navArgument("type") { type = NavType.StringType }
            )) { navBackStackEntry ->
            val verbs = when (navBackStackEntry.arguments?.getString("type") ?: "") {
                "regular" -> Pair("Regular verbs", viewModel.getRegularVerbs())
                "irregular" -> Pair("Irregular verbs", viewModel.getIrregularVerbs())
                else -> Pair("Verbs", listOf())
            }

            VerbsView(
                title = verbs.first,
                verbs = verbs.second,
                onBackClick = { navController.navigateUp() },
                onClickVerb = { verb ->
                    when (navBackStackEntry.arguments?.getString("type") ?: "") {
                        "regular" -> navController.navigate(
                            "${Routes.VerbsRegular.name}/${verb.id}/${VerbData.BaseForm.name}"
                        )
                        "irregular" -> navController.navigate(
                            "${Routes.VerbsIrregular.name}/${verb.id}/${VerbData.BaseForm.name}"
                        )
                        else -> Unit
                    }
                },
                painterIdentifier = painterIdentifier,
                speechStatus = speechStatus,
                onSpeechClick = onSpeechClick,
                onTextChangeSpeech = {
                    viewModel.updateSpeechStatus(SpeechStatus.NoSpeech)
                },
            )
        }

        composable(
            route = "${Routes.Verbs.name}/{type}/example/{form}/{verb_id}",
            arguments = listOf(
                navArgument("type") { type = NavType.StringType },
                navArgument("form") { type = NavType.StringType },
                navArgument("verb_id") { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            val verbType = navBackStackEntry.arguments?.getString("type") ?: ""
            val verbIdArg = navBackStackEntry.arguments?.getString("verb_id") ?: ""
            val form = navBackStackEntry.arguments?.getString("form") ?: ""

            var verbIndex by rememberSaveable { mutableStateOf(0) }

            val verbs = when (verbType) {
                "regular" -> viewModel.getRegularVerbs()
                "irregular" -> viewModel.getIrregularVerbs()
                else -> listOf()
            }

            val verbByForm = verbs.firstOrNull {
                it.id == verbIdArg
            }

            val examplesByForm = when(form) {
                VerbData.BaseForm.name -> verbByForm?.base?.examples
                VerbData.PastForm.name -> verbByForm?.simplePast?.examples
                VerbData.PastParticipleForm.name -> verbByForm?.pastParticiple?.examples
                VerbData.IngForm.name -> verbByForm?.ing?.examples
                else -> verbByForm?.base?.examples
            } ?: listOf()

            val verbNameByForm = when(form) {
                VerbData.BaseForm.name -> verbByForm?.base?.verb
                VerbData.PastForm.name -> verbByForm?.simplePast?.verb
                VerbData.PastParticipleForm.name -> verbByForm?.pastParticiple?.verb
                VerbData.IngForm.name -> verbByForm?.ing?.verb
                else -> verbByForm?.base?.verb
            } ?: ""

            if (examplesByForm.isNotEmpty()) {
                val hideNext = verbIndex >= examplesByForm.lastIndex
                val hidePrevious = verbIndex == 0
                SimpleExamplesView(
                    title = verbNameByForm,
                    example = examplesByForm[verbIndex],
                    onBackClick = { navController.navigateUp() },
                    hideNext = hideNext,
                    hidePrevious = hidePrevious,
                    textToSpeechStatus = textToSpeechStatus,
                    onPreviousClick = {
                        verbIndex = if (verbIndex.minus(1) in examplesByForm.indices) {
                            verbIndex.minus(1)
                        } else {
                            verbIndex
                        }
                    },
                    onNextClick = {
                        verbIndex = if (verbIndex.plus(1) in examplesByForm.indices) {
                            verbIndex.plus(1)
                        } else {
                            verbIndex
                        }
                    },
                    onListenClick = {
                        onListenClick(examplesByForm[verbIndex])
                    },
                    modifier = Modifier
                )
            }
        }

        composable(Routes.Countries.name) {
            val countries = viewModel.getCountries()
            CountryView(
                countries = countries,
                painterIdentifier = painterIdentifier,
                onClickCountry = { country ->
                    navController.navigate(
                    "${Routes.Countries.name}/${country.id}"
                ) },
                onBackClick = { navController.navigateUp() },
                speechStatus = speechStatus,
                onSpeechClick = onSpeechClick,
                onTextChangeSpeech = {
                    viewModel.updateSpeechStatus(SpeechStatus.NoSpeech)
                },
            )
        }

        composable(
            route = "${Routes.Countries.name}/{country_id}",
            arguments = listOf(
                navArgument("country_id") { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            var countryArg by rememberSaveable {
                mutableStateOf(navBackStackEntry.arguments?.getString("country_id") ?: "")
            }
            val countries = viewModel.getCountries()
            val currentCountry = countries.firstOrNull { it.id == countryArg }

            if (currentCountry != null) {
                val indexCurrent = countries.indexOfFirst { it.id == countryArg }
                CountryContentView(
                    country = currentCountry,
                    onNext = {
                        countryArg = countries.getOrElse(indexCurrent + 1) { countries[indexCurrent] }.id
                    },
                    onPrevious = {
                        countryArg = countries.getOrElse(indexCurrent - 1) { countries[indexCurrent] }.id
                    },
                    onListen = { onListenClick(it) },
                    onNavBack = { navController.navigateUp() },
                    onOpenMap = {
                        onOpenMaps(countries[indexCurrent])
                    },
                    onGallery = {
                        navController.navigate(
                        "${Routes.Gallery.name}/${currentCountry.name}"
                        )
                    },
                    onShareClick = {
                        showQRDialog("${Routes.Countries.name}/${currentCountry.id}")
                    },
                    hasNext = indexCurrent < countries.lastIndex,
                    hasPrevious = indexCurrent != 0,
                    painterIdentifier = painterIdentifier,
                    textToSpeechStatus = textToSpeechStatus,
                )
            }
        }

        composable(Routes.Animals.name) {
            val animals = viewModel.getAnimals()
            AnimalsView(
                animals = animals,
                painterIdentifier = painterIdentifier,
                onClickAnimal = { animal ->
                    navController.navigate(
                    "${Routes.Animals.name}/${animal.id}"
                    )
                },
                onBackClick = {
                    navController.navigateUp()
                },
                speechStatus = speechStatus,
                onSpeechClick = onSpeechClick,
                onTextChangeSpeech = {
                    viewModel.updateSpeechStatus(SpeechStatus.NoSpeech)
                }
            )
        }

        composable(
            route = "${Routes.Animals.name}/{animal_id}",
            arguments = listOf(
                navArgument("animal_id") { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            var animalArg by rememberSaveable {
                mutableStateOf(navBackStackEntry.arguments?.getString("animal_id") ?: "")
            }
            val animals = viewModel.getAnimals()
            val currentAnimal = animals.firstOrNull { it.id == animalArg }

            if (currentAnimal != null) {
                val indexCurrent = animals.indexOfFirst { it.id == animalArg }
                AnimalContentView(
                    animal = currentAnimal,
                    onNext = {
                        animalArg = animals.getOrElse(indexCurrent + 1) { animals[indexCurrent] }.id
                    },
                    onPrevious = {
                        animalArg = animals.getOrElse(indexCurrent - 1) { animals[indexCurrent] }.id
                    },
                    onListen = { onListenClick(currentAnimal.name) },
                    onNavBack = { navController.navigateUp() },
                    onOpenLink = { onOpenLink(currentAnimal.name) },
                    onGallery = { navController.navigate(
                        "${Routes.Gallery.name}/${currentAnimal.name}"
                    ) },
                    onShareClick = {
                         showQRDialog("${Routes.Animals.name}/${currentAnimal.id}")
                    },
                    hasNext = indexCurrent < animals.lastIndex,
                    hasPrevious = indexCurrent != 0,
                    painterIdentifier = painterIdentifier,
                    textToSpeechStatus = textToSpeechStatus,
                )
            }
        }

        composable(Routes.Colors.name) {
            val colors = viewModel.getColors()
            ColorsView(
                colorHazels = colors,
                onBackClick = { navController.navigateUp() },
                onClickColor = { color ->
                    navController.navigate(
                    "${Routes.Colors.name}/${color.id}"
                ) },
                speechStatus = speechStatus,
                onSpeechClick = onSpeechClick,
                onTextChangeSpeech = {
                    viewModel.updateSpeechStatus(SpeechStatus.NoSpeech)
                },
            )
        }

        composable(
            route = "${Routes.Gallery.name}/{query}",
            arguments = listOf(
                navArgument("query") { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            val queryArg = navBackStackEntry.arguments?.getString("query") ?: ""
            val result by viewModel.galleryStatus.collectAsState()
            val context = LocalContext.current
            DisposableEffect(queryArg) {
                if (queryArg.isNotEmpty()) {
                    viewModel.updateGalleryStatus(GalleryStatus.Loading)
                    viewModel.getToken { token ->
                        val searchHelper = SearchKitHelper(context)
                        val images = searchHelper.searchImage(token, queryArg)
                        viewModel.updateGalleryStatus(GalleryStatus.Success(images.mapNotNull {
                            it.sourceImage.imageContentUrl
                        }))
                    }
                }
                onDispose {  }
            }

            GalleryView(
                title = queryArg,
                galleryStatus = result,
                painterIdentifier = painterIdentifier,
                onBackClick = { navController.navigateUp() }
            )
        }

        composable(
            route = "${Routes.Colors.name}/color-example/{color}",
            arguments = listOf(
                navArgument("color") { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            val colorArg =navBackStackEntry.arguments?.getString("color") ?: ""
            val colors = viewModel.getColors()
            val colorByCode = colors.firstOrNull { it.code == colorArg }
            var colorExample by rememberSaveable { mutableStateOf(colorByCode?.examples?.firstOrNull() ?: "") }

            if (colorExample.isNotEmpty() && colorByCode != null) {
                val indexOfExample = colorByCode.examples.indexOfFirst { it == colorExample }
                val hideNext = indexOfExample >= colorByCode.examples.lastIndex
                val hidePrevious = indexOfExample == 0
                SimpleExamplesView(
                    title = colorByCode.name,
                    example = colorExample,
                    onBackClick = { navController.navigateUp() },
                    hideNext = hideNext,
                    hidePrevious = hidePrevious,
                    textToSpeechStatus = textToSpeechStatus,
                    onPreviousClick = {
                        colorExample = colorByCode.examples.getOrElse(indexOfExample - 1) { colorExample }
                    },
                    onNextClick = {
                        colorExample = colorByCode.examples.getOrElse(indexOfExample + 1) { colorExample }
                    },
                    onListenClick = {
                        onListenClick(colorExample)
                    },
                    modifier = Modifier
                )
            }
        }

        composable(
            route = "${Routes.Colors.name}/{color_id}",
            arguments = listOf(
                navArgument("color_id") { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            val colors = viewModel.getColors()
            var colorIdArg by rememberSaveable {
                mutableStateOf(navBackStackEntry.arguments?.getString("color_id") ?: "")
            }
            val currentColor = viewModel.getColorById(colorIdArg)

            currentColor?.let { color ->
                val currentIndex = colors.indexOfFirst {
                    it.code == color.code
                }
                val hasNext = currentIndex < colors.lastIndex
                val hasPrevious = currentIndex != 0
                OneColorView(
                    colorHazel = color,
                    onOpenLink = { onOpenLink(color.name) },
                    onNavBack = { navController.navigateUp() },
                    onListen = onListenClick,
                    textToSpeechStatus = textToSpeechStatus,
                    onSeeExamples = {
                        navController.navigate(
                            "${Routes.Colors.name}/color-example/${color.code}"
                        )
                    },
                    onGallery = {
                        navController.navigate(
                            "${Routes.Gallery.name}/${color.name}"
                        )
                    },
                    onShareClick = {
                        showQRDialog("${Routes.Colors.name}/${currentColor.id}")
                    },
                    hasNext = hasNext,
                    hasPrevious = hasPrevious,
                    onNextClick = {
                        colorIdArg = colors.getOrElse(currentIndex + 1) { color }.code
                    },
                    onPreviousClick = {
                        colorIdArg = colors.getOrElse(currentIndex - 1) { color }.code
                    },
                )
            }
        }

        composable(Routes.Main.name) {
            val searchResult by viewModel.searchStatus.collectAsState()
            MainScreen(
                darkMode = darkMode,
                status = hazelContentStatus,
                painterIdentifier = painterIdentifier,
                temperature = weatherStatus,
                onSettingsClick = { navController.navigate(Routes.Settings.name) },
                searchStatus = searchResult,
                onSearchQuery = { query ->
                    viewModel.searchQuery(listOf(query))
                },
                onNavigate = { route ->
                    navController.navigate(route)
                },
                onDarkModeChange = { darkMode ->
                    onSelectDarkMode(darkMode)
                },
                onCheckWeather = onRequestWeather,
                onSpeechClick = onSpeechClick,
                speechStatus = speechStatus,
                clearSpeechResult = {
                    viewModel.updateSpeechStatus(SpeechStatus.NoSpeech)
                },
                onClickTime = {
                    navController.navigate(Routes.Time.name)
                }
            )
        }

        composable(
            route = "${Routes.UsefulExpressions.name}/{category_id}",
            arguments = listOf(navArgument("category_id") {
                type = NavType.StringType
            })
        ) { navBackStackEntry ->
            val categoryId = navBackStackEntry.arguments?.getString("category_id") ?: ""

            Surface(
                color = MaterialTheme.colors.background,
                modifier = Modifier.fillMaxSize()
            ) {
                viewModel.getUsefulExpressionCategoryId(categoryId)?.let { phrases ->
                    UsefulExpressionsView(
                        usefulPhrase = phrases,
                        onBackClick = {
                            navController.navigateUp()
                        },
                        onClickPhrase = { phrase ->
                            navController.navigate(
                                "${Routes.UsefulExpressionsPhrase.name}/${categoryId}/${phrase.id}"
                            )
                        },
                        speechStatus = speechStatus,
                        onSpeechClick = onSpeechClick,
                        onTextChangeSpeech = {
                            viewModel.updateSpeechStatus(SpeechStatus.NoSpeech)
                        }
                    )
                }
            }
        }

        composable(
            route = "${Routes.UsefulExpressionsPhrase.name}/{category_id}/{phrase_id}",
            arguments = listOf(
                navArgument("category_id") { type = NavType.StringType },
                navArgument("phrase_id") { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            var phraseIdArg by rememberSaveable {
                mutableStateOf(navBackStackEntry.arguments?.getString("phrase_id") ?: "")
            }
            val categoryIdArg = navBackStackEntry.arguments?.getString("category_id") ?: ""

            if (phraseIdArg.isNotEmpty()) {
                viewModel.getUsefulExpressionCategoryId(categoryIdArg)?.let { usefulPhrases ->
                    val currentIndex = usefulPhrases.phrases.indexOfFirst { it.id == phraseIdArg }
                    val hideNext = usefulPhrases.phrases.lastIndex <= currentIndex
                    val hidePrevious = currentIndex == 0
                    val currentPhrase = usefulPhrases.phrases.getOrElse(currentIndex) { Phrase.empty }

                    PhraseView(
                        currentPhrase = currentPhrase,
                        hideNext = hideNext,
                        hidePrevious = hidePrevious,
                        textToSpeechStatus = textToSpeechStatus,
                        onNextClick = {
                            if (currentIndex != -1) {
                                phraseIdArg = usefulPhrases.phrases.getOrElse(currentIndex + 1) {
                                    usefulPhrases.phrases[currentIndex]
                                }.id
                            }
                        },
                        onPreviousClick = {
                            if (currentIndex != -1) {
                                phraseIdArg = usefulPhrases.phrases.getOrElse(currentIndex - 1) {
                                    usefulPhrases.phrases[currentIndex]
                                }.id
                            }
                        },
                        onListenClick = {
                            onListenClick(currentPhrase.expression)
                        },
                        onNavigate = {
                            navController.navigateUp()
                        },
                        onShareClick = {
                            showQRDialog("${Routes.UsefulExpressionsPhrase.name}/${categoryIdArg}/${phraseIdArg}")
                        },
                    )
                }
            }
        }

        composable(route = Routes.Settings.name) {
            SettingsView(
                onBackClick = { navController.navigateUp() },
                onSelectDictionary = onSelectDictionary,
                onSelectColorScheme = onSelectColorScheme,
                onSelectDarkMode = onSelectDarkMode,
                colorVariant = colorScheme,
                dictionaries = dictionary,
                darkMode = darkMode,
            )
        }

        composable(route = Routes.Seasons.name) {
            val seasons = viewModel.getSeasons()
            SeasonsView(
                seasons = seasons,
                speechStatus = speechStatus,
                onSpeechClick = onSpeechClick,
                painterIdentifier = painterIdentifier,
                onBackClick = { navController.navigateUp() },
                onTextChangeSpeech = {
                    viewModel.updateSpeechStatus(SpeechStatus.NoSpeech)
                },
                onClickSeason = { season ->
                    navController.navigate("${Routes.Seasons.name}/${season.id}")
                }
            )
        }

        composable(route = Routes.Time.name) {
            val scope = rememberCoroutineScope()
            var timeInWords = listOf<String>()
            var timeInNumbersPMAM by rememberSaveable { mutableStateOf("") }
            var currentTimeInWordIndex by rememberSaveable { mutableStateOf(0) }

            fun updateTime() {
                val localDateTime = LocalDateTime.now()
                val timeText = TimeText(localDateTime.hour, localDateTime.minute)
                timeInWords = timeText.getTimePhases()
                timeInNumbersPMAM = timeText.getTimeAMPM(breakLine = true)
            }
            updateTime()
            SideEffect {
                scope.launch {
                    Timer().schedule(
                        delay = 1000,
                        period = 1000
                    ) {
                        updateTime()
                    }
                }
            }
            if(timeInWords.isNotEmpty()) {
                val hasPrevious = currentTimeInWordIndex > 0
                val hasNext = currentTimeInWordIndex < timeInWords.lastIndex
                TimeView(
                    onBackClick = { navController.navigateUp() },
                    onPreviousClick = {
                        if (currentTimeInWordIndex > 0) {
                            currentTimeInWordIndex--
                        }
                    },
                    onNextClick = {
                        if (currentTimeInWordIndex < timeInWords.lastIndex) {
                            currentTimeInWordIndex++
                        }
                    },
                    onListen = { onListenClick(it) },
                    hasPrevious = hasPrevious,
                    hasNext = hasNext,
                    textToSpeechStatus = textToSpeechStatus,
                    timeInNumbersPMAM = timeInNumbersPMAM,
                    timeInWords = timeInWords.getOrElse(currentTimeInWordIndex) { "" },
                    onShareClick = { showQRDialog(Routes.Time.name) },
                )
            }
        }

        composable(
            route = "${Routes.Seasons.name}/{season_id}",
            arguments = listOf(
                navArgument("season_id") { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            var seasonIdArg by rememberSaveable {
                mutableStateOf(navBackStackEntry.arguments?.getString("season_id") ?: "")
            }
            val currentSeason = viewModel.getSeasonById(seasonIdArg)
            val seasons = viewModel.getSeasons()

            currentSeason?.let {
                val currentIndex = seasons.indexOfFirst { seasonFromList ->
                    it.id == seasonFromList.id
                }
                val hasNext = currentIndex < seasons.lastIndex
                val hasPrevious = currentIndex != 0
                SeasonContentView(
                    season = currentSeason,
                    onOpenLink = { onOpenLink(currentSeason.name) },
                    onNavBack = { navController.navigateUp() },
                    onListen = { onListenClick(currentSeason.name) },
                    onGallery = {
                        navController.navigate(
                            "${Routes.Gallery.name}/${currentSeason.name}"
                        )
                    },
                    hasNext = hasNext,
                    hasPrevious = hasPrevious,
                    onNextClick = {
                        seasonIdArg = seasons.getOrElse(currentIndex + 1) { currentSeason }.id
                    },
                    onPreviousClick = {
                        seasonIdArg = seasons.getOrElse(currentIndex - 1) { currentSeason }.id
                    },
                    onShareClick = {
                        showQRDialog("${Routes.Seasons.name}/${currentSeason.id}")
                    },
                    panorama = panoramaInterface,
                    onPanoramaClick = { onPanoramaClick(currentSeason) },
                    textToSpeechStatus = textToSpeechStatus,
                )
            }
        }

        composable(route = Routes.Weather.name) {
            val weathers = viewModel.getWeather()
            WeatherView(
                weathers = weathers,
                speechStatus = speechStatus,
                onTextChangeSpeech = {
                    viewModel.updateSpeechStatus(SpeechStatus.NoSpeech)
                },
                onClickWeather = { weather ->
                    navController.navigate("${Routes.Weather.name}/${weather.id}")
                },
                painterIdentifier = painterIdentifier,
                onBackClick = { navController.navigateUp() },
                onSpeechClick = onSpeechClick,
            )
        }

        composable(
            route = "${Routes.Weather.name}/{weather_id}",
            arguments = listOf(
                navArgument("weather_id") { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            var weatherIdArg by rememberSaveable {
                mutableStateOf(navBackStackEntry.arguments?.getString("weather_id") ?: "")
            }
            val currentWeather = viewModel.getWeatherById(weatherIdArg)
            val weathers = viewModel.getWeather()

            currentWeather?.let {
                val currentIndex = weathers.indexOfFirst { weatherFromList ->
                    it.id == weatherFromList.id
                }
                val hasNext = currentIndex < weathers.lastIndex
                val hasPrevious = currentIndex != 0
                WeatherContentView(
                    weather = currentWeather,
                    onNavBack = { navController.navigateUp() },
                    onListen = { onListenClick(currentWeather.name) },
                    hasNext = hasNext,
                    hasPrevious = hasPrevious,
                    onNextClick = {
                        weatherIdArg = weathers.getOrElse(currentIndex + 1) { currentWeather }.id
                    },
                    onPreviousClick = {
                        weatherIdArg = weathers.getOrElse(currentIndex - 1) { currentWeather }.id
                    },
                    onShareClick = {
                        showQRDialog("${Routes.Weather.name}/${currentWeather.id}")
                    },
                    textToSpeechStatus = textToSpeechStatus,
                    painterIdentifier = painterIdentifier,
                )
            }
        }

        composable(route = Routes.Camera.name) {
            val textRecognition by viewModel.textRecognitionStatus.collectAsState()
            val barcodeStatus by viewModel.barcodeStatus.collectAsState()
            CameraToolView(
                onBackClick = { navController.navigateUp() },
                textRecognition = textRecognition,
                barcodeStatus = barcodeStatus,
                onAnalysisCaptureError = { cameraFeature ->
                    when (cameraFeature) {
                        CameraFeature.QRReader -> viewModel.updateBarcodeStatus(BarcodeDetectorStatus.Error)
                        CameraFeature.TextRecognizer -> viewModel.updateTextRecognitionStatus(TextRecognitionStatus.Error)
                    }
                },
                onAnalysisStart = { cameraFeature ->
                    when (cameraFeature) {
                        CameraFeature.QRReader -> viewModel.updateBarcodeStatus(BarcodeDetectorStatus.Processing)
                        CameraFeature.TextRecognizer -> viewModel.updateTextRecognitionStatus(TextRecognitionStatus.Processing)
                    }
                },
                onAnalysisCapture = { bitmap, cameraFeature ->
                    onAnalysisCapture(bitmap, cameraFeature)
                },
            )
        }

        composable(
            route = "${Routes.Camera.name}/results/{type_result}",
            arguments = listOf(
                navArgument("type_result") { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            Text("hello")
            val typeResult = navBackStackEntry.arguments?.getString("type_result")
            val textRecognition by viewModel.textRecognitionStatus.collectAsState()
            val barcodeStatus by viewModel.barcodeStatus.collectAsState()
            val searchStatus by viewModel.searchStatus.collectAsState()

            val result = when(typeResult) {
                "text" -> (textRecognition as TextRecognitionStatus.Result).value
                "qr" -> (barcodeStatus as BarcodeDetectorStatus.Result).value
                else -> ""
            }

            LaunchedEffect(key1 = result) {
                viewModel.searchQuery(result.split(" ", ".", "\n", "_"))
            }
            ResultCameraView(
                result = result,
                onBackClick = { navController.navigateUp() },
                onCopy = { onCopy(result) },
                painterIdentifier = painterIdentifier,
                onNavigate = { route ->
                    navController.navigate(route) },
                searchStatus = searchStatus,
            )
        }
    }
}