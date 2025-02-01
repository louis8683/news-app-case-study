package com.louislu.newsappcasestudy

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.louislu.core.presentation.detail.DetailScreenRoot

@Composable
fun NavigationRoot(
    navController: NavHostController,
) {

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute by remember {
        derivedStateOf { currentBackStackEntry?.destination?.route }
    }

    // Define when

    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        mainGraph(navController)
    }
}

private fun NavGraphBuilder.mainGraph(navController: NavHostController) {
    navigation(
        startDestination = "mainScreen",
        route = "main"
    ) {
        composable(route = "mainScreen") {
            MainScreen(
                navigateToDetailWithNews = { news ->
                    navController.navigate("detail?order=${news.order}")
                },
                navigateToDetailWithSaved = { news ->
                    navController.navigate("detail?title=${news.title}")
                }
            )
        }

//        composable(route = "news") {
//            MainScreenRoot(
//                onNewsCardClick = { news ->
//                    navController.navigate("detail/${news.order}")
//                },
//            )
//        }

        composable(
            route = "detail?order={order}&title={title}",
            arguments = listOf(
                navArgument("order") {
                    type = NavType.IntType
                    defaultValue = -1
                },
                navArgument("title") {
                    type = NavType.StringType
                    defaultValue = null
                    nullable = true
                },
            )
        ) {
            DetailScreenRoot() // Pass order to DetailScreenRoot
        }
    }
}