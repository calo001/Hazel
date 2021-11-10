package com.github.calo001.hazel.ui.main

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
import com.github.calo001.hazel.routes.Routes
import com.github.calo001.hazel.ui.animals.AnimalContentView
import com.github.calo001.hazel.ui.animals.AnimalsView
import com.github.calo001.hazel.ui.colors.ColorExamplesView
import com.github.calo001.hazel.ui.colors.ColorsView
import com.github.calo001.hazel.ui.colors.OneColorView
import com.github.calo001.hazel.ui.gallery.GalleryView
import com.github.calo001.hazel.ui.usefulexp.PhraseView
import com.github.calo001.hazel.ui.usefulexp.UsefulExpressionsView
import com.github.calo001.hazel.util.PainterIdentifier

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
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Main.name
    ) {
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
            val colorArg = navBackStackEntry.arguments?.getString("query") ?: ""
            val result by viewModel.galleryStatus.collectAsState()
            DisposableEffect(colorArg) {
                if (colorArg.isNotEmpty()) {
                    viewModel.search(colorArg)
                }
                onDispose {  }
            }

            val resultList = (result as? GalleryStatus.Success)?.content?.results ?: listOf()

            GalleryView(
                title = colorArg,
                unsplashResult = resultList,
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
                ColorExamplesView(
                    colorName = colorByCode.name,
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
            MainScreen(
                status = hazelContentStatus,
                painterIdentifier = painterIdentifier,
                onNavigate = { route, subsection ->
                    if (route is Routes.UsefulExpressions) {
                        navController.navigate(
                            "${route.name}/${subsection}"
                        )
                    } else {
                        navController.navigate(route.name)
                    }
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
    }
}