package com.github.calo001.hazel.ui.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.github.calo001.hazel.routes.Routes
import com.github.calo001.hazel.ui.usefulexp.PhraseView
import com.github.calo001.hazel.ui.usefulexp.UsefulExpressionsView
import com.github.calo001.hazel.util.PainterIdentifier

@ExperimentalMaterialApi
@Composable
fun Router(
    navController: NavHostController,
    hazelContentStatus: HazelContentStatus,
    painterIdentifier: PainterIdentifier,
    onListenClick: (String) -> Unit,
    viewModel: MainViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Main.name
    ) {
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
                        }
                    )
                }
            }
        }
    }
}