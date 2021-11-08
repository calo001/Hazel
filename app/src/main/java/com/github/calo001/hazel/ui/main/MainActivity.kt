package com.github.calo001.hazel.ui.main

import android.content.res.Resources
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.github.calo001.hazel.config.ColorVariant
import com.github.calo001.hazel.routes.Routes
import com.github.calo001.hazel.ui.theme.HazelTheme
import com.github.calo001.hazel.R
import com.github.calo001.hazel.ui.common.HazelToolbar
import com.github.calo001.hazel.ui.common.SearchBar
import com.github.calo001.hazel.util.PainterIdentifier
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val hazelDb = resources.openRawResource(R.raw.hazel)
        viewModel.loadHazelContent(hazelDb)

        setContent {
            val systemUiController = rememberSystemUiController()
            val useDarkIcons = MaterialTheme.colors.isLight

            SideEffect {
                // Update all of the system bar colors to be transparent, and use
                // dark icons if we're in light theme
                systemUiController.setSystemBarsColor(
                    color = Color.Transparent,
                    darkIcons = useDarkIcons
                )

                systemUiController.setStatusBarColor(
                    color = Color.Transparent,
                    darkIcons = useDarkIcons
                )

                systemUiController.setNavigationBarColor(
                    color = Color.Transparent,
                    darkIcons = useDarkIcons
                )
            }

            HazelTheme(
                colorVariant = ColorVariant.Green
            ) {
                val navController = rememberNavController()
                val painterIdentifier = PainterIdentifier(
                    resources = resources,
                    packageName = packageName,
                    default = R.drawable.ic_launcher_background
                )
                val hazelContentStatus by viewModel.hazelContent.collectAsState()

                Surface(
                    color = MaterialTheme.colors.background,
                ) {
                    Router(
                        navController = navController,
                        hazelContentStatus = hazelContentStatus,
                        painterIdentifier = painterIdentifier
                    )
                }
            }
        }
    }

    @ExperimentalMaterialApi
    @Composable
    private fun Router(
        navController: NavHostController,
        hazelContentStatus: HazelContentStatus,
        painterIdentifier: PainterIdentifier
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
                route = "useful_expressions/{name}",
                arguments = listOf(navArgument("name") {
                    type = NavType.StringType
                })
            ) { navBackStackEntry ->
                val typeOfUsefulExp by remember {
                    mutableStateOf(
                        navBackStackEntry.arguments?.getString("name") ?: ""
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
)
@Composable
fun DefaultPreview() {
    HazelTheme(
        colorVariant = ColorVariant.Green
    ) {
        Surface(color = MaterialTheme.colors.background) {
            //ContentT(resources, packageName)
        }
    }
}