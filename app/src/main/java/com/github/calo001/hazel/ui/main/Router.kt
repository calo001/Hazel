package com.github.calo001.hazel.ui.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.github.calo001.hazel.config.ColorVariant
import com.github.calo001.hazel.config.DarkMode
import com.github.calo001.hazel.routes.Routes
import com.github.calo001.hazel.ui.animals.AnimalContentView
import com.github.calo001.hazel.ui.animals.AnimalsView
import com.github.calo001.hazel.ui.colors.SimpleExamplesView
import com.github.calo001.hazel.ui.colors.ColorsView
import com.github.calo001.hazel.ui.colors.OneColorView
import com.github.calo001.hazel.ui.countries.CountryContentView
import com.github.calo001.hazel.ui.countries.CountryView
import com.github.calo001.hazel.ui.gallery.GalleryView
import com.github.calo001.hazel.ui.settings.Dictionaries
import com.github.calo001.hazel.ui.settings.SettingsView
import com.github.calo001.hazel.ui.usefulexp.PhraseView
import com.github.calo001.hazel.ui.usefulexp.UsefulExpressionsView
import com.github.calo001.hazel.ui.verbs.VerbContentView
import com.github.calo001.hazel.ui.verbs.VerbData
import com.github.calo001.hazel.ui.verbs.VerbsView
import com.github.calo001.hazel.util.PainterIdentifier

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
    onOpenMaps: (String) -> Unit,
    onSelectColorScheme: (ColorVariant) -> Unit,
    onSelectDictionary: (Dictionaries) -> Unit,
    onSelectDarkMode: (DarkMode) -> Unit,
    darkMode: DarkMode,
    colorScheme: ColorVariant,
    dictionary: Dictionaries,
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Main.name
    ) {
        composable(
            route = "${Routes.Verbs.name}/{type}/{verb}/{form}",
            arguments = listOf(
                navArgument("type") { type = NavType.StringType },
                navArgument("verb") { type = NavType.StringType },
                navArgument("form") { type = NavType.StringType },
            )
        ) { navBackStackEntry ->
            var verbBaseFormArg by rememberSaveable {
                mutableStateOf(navBackStackEntry.arguments?.getString("verb") ?: "")
            }

            val type = navBackStackEntry.arguments?.getString("type") ?: ""
            val form = navBackStackEntry.arguments?.getString("form") ?: VerbData.BaseForm.name

            val verbs = when (type) {
                "regular" -> Pair("Regular verbs", viewModel.getRegularVerbs())
                "irregular" -> Pair("Irregular verbs", viewModel.getIrregularVerbs())
                else -> Pair("Verbs", listOf())
            }

            val currentVerb = verbs.second.firstOrNull { it.base.verb == verbBaseFormArg }

            if (currentVerb != null) {
                val indexCurrent = verbs.second.indexOfFirst { it.base.verb == verbBaseFormArg }
                VerbContentView(
                    verb = currentVerb,
                    selectedForm = form,
                    onNext = {
                        verbBaseFormArg = verbs.second.getOrElse(indexCurrent + 1) { verbs.second[indexCurrent] }.base.verb
                    },
                    onPrevious = {
                        verbBaseFormArg = verbs.second.getOrElse(indexCurrent - 1) { verbs.second[indexCurrent] }.base.verb
                    },
                    onOpenLink = { term ->
                        onOpenLink(term)
                    },
                    onSeeExamples = { form ->
                        navController.navigate("${when(type) {
                                "regular" -> Routes.VerbsRegular.name
                                "irregular" -> Routes.VerbsIrregular.name
                                else -> Routes.VerbsRegular.name }
                            }/example/${form}/${
                                when(form) {
                                    VerbData.BaseForm.name -> currentVerb.base.verb
                                    VerbData.PastForm.name -> currentVerb.simplePast.verb
                                    VerbData.PastParticipleForm.name -> currentVerb.pastParticiple.verb
                                    VerbData.IngForm.name -> currentVerb.ing.verb
                                    else -> currentVerb.base.verb
                                }
                            }"
                        )
                    },
                    onListen = { onListenClick(it) },
                    onNavBack = { navController.navigateUp() },
                    hasNext = indexCurrent < verbs.second.lastIndex,
                    hasPrevious = indexCurrent != 0,
                    painterIdentifier = painterIdentifier,
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
                            "${Routes.VerbsRegular.name}/${verb.base.verb}/${VerbData.BaseForm.name}"
                        )
                        "irregular" -> navController.navigate(
                            "${Routes.VerbsIrregular.name}/${verb.base.verb}/${VerbData.BaseForm.name}"
                        )
                        else -> Unit
                    }
                },
                painterIdentifier = painterIdentifier
            )
        }

        composable(
            route = "${Routes.Verbs.name}/{type}/example/{form}/{verb}",
            arguments = listOf(
                navArgument("type") { type = NavType.StringType },
                navArgument("form") { type = NavType.StringType },
                navArgument("verb") { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            val verbType = navBackStackEntry.arguments?.getString("type") ?: ""
            val verbArg = navBackStackEntry.arguments?.getString("verb") ?: ""
            val form = navBackStackEntry.arguments?.getString("form") ?: ""

            var verbIndex by rememberSaveable { mutableStateOf(0) }

            val verbs = when (verbType) {
                "regular" -> viewModel.getRegularVerbs()
                "irregular" -> viewModel.getIrregularVerbs()
                else -> listOf()
            }

            val verbByForm = verbs.firstOrNull() {
                when(form) {
                    VerbData.BaseForm.name -> it.base.verb == verbArg
                    VerbData.PastForm.name -> it.simplePast.verb == verbArg
                    VerbData.PastParticipleForm.name -> it.pastParticiple.verb == verbArg
                    VerbData.IngForm.name -> it.ing.verb == verbArg
                    else -> it.base.verb == verbArg
                }
            }

            val examplesByForm = when(form) {
                VerbData.BaseForm.name -> verbByForm?.base?.examples
                VerbData.PastForm.name -> verbByForm?.simplePast?.examples
                VerbData.PastParticipleForm.name -> verbByForm?.pastParticiple?.examples
                VerbData.IngForm.name -> verbByForm?.ing?.examples
                else -> verbByForm?.base?.examples
            } ?: listOf()

            if (examplesByForm.isNotEmpty()) {
                val hideNext = verbIndex >= examplesByForm.lastIndex
                val hidePrevious = verbIndex == 0
                SimpleExamplesView(
                    title = verbArg,
                    example = examplesByForm[verbIndex],
                    onBackClick = { navController.navigateUp() },
                    hideNext = hideNext,
                    hidePrevious = hidePrevious,
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
                    "${Routes.Countries.name}/${country.name}"
                ) },
                onBackClick = { navController.navigateUp() }
            )
        }

        composable(
            route = "${Routes.Countries.name}/{country}",
            arguments = listOf(
                navArgument("country") { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            var countryArg by rememberSaveable {
                mutableStateOf(navBackStackEntry.arguments?.getString("country") ?: "")
            }
            val countries = viewModel.getCountries()
            val currentCountry = countries.firstOrNull { it.name == countryArg }

            if (currentCountry != null) {
                val indexCurrent = countries.indexOfFirst { it.name == countryArg }
                CountryContentView(
                    country = currentCountry,
                    onNext = {
                        countryArg = countries.getOrElse(indexCurrent + 1) { countries[indexCurrent] }.name
                    },
                    onPrevious = {
                        countryArg = countries.getOrElse(indexCurrent - 1) { countries[indexCurrent] }.name
                    },
                    onListen = { onListenClick(it) },
                    onNavBack = { navController.navigateUp() },
                    onOpenMap = {
                        onOpenMaps(countries[indexCurrent].linkMaps)
                    },
                    onGallery = {
                        navController.navigate(
                        "${Routes.Gallery.name}/${currentCountry.name}"
                        )
                    },
                    hasNext = indexCurrent < countries.lastIndex,
                    hasPrevious = indexCurrent != 0,
                    painterIdentifier = painterIdentifier,
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
                    "${Routes.Animals.name}/${animal.name}"
                    )
                },
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }

        composable(
            route = "${Routes.Animals.name}/{animal}",
            arguments = listOf(
                navArgument("animal") { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            var animalArg by rememberSaveable {
                mutableStateOf(navBackStackEntry.arguments?.getString("animal") ?: "")
            }
            val animals = viewModel.getAnimals()
            val currentAnimal = animals.firstOrNull { it.name == animalArg }

            if (currentAnimal != null) {
                val indexCurrent = animals.indexOfFirst { it.name == animalArg }
                AnimalContentView(
                    animal = currentAnimal,
                    onNext = {
                        animalArg = animals.getOrElse(indexCurrent + 1) { animals[indexCurrent] }.name
                    },
                    onPrevious = {
                        animalArg = animals.getOrElse(indexCurrent - 1) { animals[indexCurrent] }.name
                    },
                    onListen = { onListenClick(currentAnimal.name) },
                    onNavBack = { navController.navigateUp() },
                    onOpenLink = { onOpenLink(currentAnimal.name) },
                    onGallery = { navController.navigate(
                        "${Routes.Gallery.name}/${currentAnimal.name}"
                    ) },
                    hasNext = indexCurrent < animals.lastIndex,
                    hasPrevious = indexCurrent != 0,
                    painterIdentifier = painterIdentifier,
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
                    "${Routes.Colors.name}/${color.code}"
                ) }
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
            DisposableEffect(queryArg) {
                if (queryArg.isNotEmpty()) {
                    viewModel.searchUnsplash(queryArg)
                }
                onDispose {  }
            }

            GalleryView(
                title = queryArg,
                unsplashResult = result,
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
            val colorByCode = colors.firstOrNull() { it.code == colorArg }
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
            route = "${Routes.Colors.name}/{color}",
            arguments = listOf(
                navArgument("color") { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            val colors = viewModel.getColors()
            var colorArg by rememberSaveable {
                mutableStateOf(navBackStackEntry.arguments?.getString("color") ?: "")
            }
            val currentColor = viewModel.getColorByCode(colorArg)

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
                    hasNext = hasNext,
                    hasPrevious = hasPrevious,
                    onNextClick = {
                        colorArg = colors.getOrElse(currentIndex + 1) { color }.code
                    },
                    onPreviousClick = {
                        colorArg = colors.getOrElse(currentIndex - 1) { color }.code
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
                onSettingsClick = { navController.navigate(Routes.Settings.name) },
                searchStatus = searchResult,
                onSearchQuery = { query ->
                    viewModel.searchQuery(query)
                },
                onNavigate = { route ->
                    navController.navigate(route)
                },
                onDarkModeChange = { darkMode ->
                    onSelectDarkMode(darkMode)
                }
            )
        }

        composable(
            route = "useful_expressions/{category}",
            arguments = listOf(navArgument("category") {
                type = NavType.StringType
            })
        ) { navBackStackEntry ->
            val typeOfUsefulExp = navBackStackEntry.arguments?.getString("category") ?: ""

            Surface(
                color = MaterialTheme.colors.background,
                modifier = Modifier.fillMaxSize()
            ) {
                viewModel.getUsefulExpressionCategory(typeOfUsefulExp)?.let { phrases ->
                    UsefulExpressionsView(
                        usefulPhrase = phrases,
                        onBackClick = {
                            navController.navigateUp()
                        },
                        onClickPhrase = { phrase ->
                            navController.navigate(
                                "${Routes.UsefulExpressionsPhrase.name}/${phrases.category}/${phrase.expression}"
                            )
                        }
                    )
                }
            }
        }

        composable(
            route = "${Routes.UsefulExpressionsPhrase.name}/{category}/{phrase}",
            arguments = listOf(
                navArgument("category") { type = NavType.StringType },
                navArgument("phrase") { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            var phraseArg by rememberSaveable {
                mutableStateOf(navBackStackEntry.arguments?.getString("phrase") ?: "")
            }
            val categoryArg = navBackStackEntry.arguments?.getString("category") ?: ""

            if (phraseArg.isNotEmpty()) {
                viewModel.getUsefulExpressionCategory(categoryArg)?.let { usefulPhrases ->
                    val currentIndex = usefulPhrases.phrases.indexOfFirst { it.expression .contains(phraseArg) }
                    val hideNext = usefulPhrases.phrases.lastIndex <= currentIndex
                    val hidePrevious = currentIndex == 0
                    val currentPhrase = usefulPhrases.phrases.find { it.expression.contains(phraseArg) }

                    PhraseView(
                        currentPhrase = currentPhrase,
                        hideNext = hideNext,
                        hidePrevious = hidePrevious,
                        onNextClick = {
                            val index = usefulPhrases.phrases.indexOfFirst { it.expression .contains(phraseArg) }
                            if (index != -1) {
                                phraseArg = usefulPhrases.phrases.getOrElse(index + 1) {
                                    usefulPhrases.phrases[index]
                                }.expression
                            }
                        },
                        onPreviousClick = {
                            val index = usefulPhrases.phrases.indexOfFirst { it.expression.contains(phraseArg) }
                            if (index != -1) {
                                phraseArg = usefulPhrases.phrases.getOrElse(index - 1) {
                                    usefulPhrases.phrases[index]
                                }.expression
                            }
                        },
                        onListenClick = {
                            onListenClick(currentPhrase?.expression ?: "")
                        },
                        onNavigate = {
                            navController.navigateUp()
                        }
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
    }
}