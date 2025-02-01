package com.louislu.newsappcasestudy

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.louislu.news.presentation.detail.DetailScreenRoot
import com.louislu.newsappcasestudy.parcelable.ParcelableNews
import timber.log.Timber

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
                    navController.navigate("detail/${news.order}")
                },
                navigateToDetailWithSaved = { /*TODO*/ }
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
            route = "detail/{order}",
            arguments = listOf(navArgument("order") { type = NavType.IntType }) // Pass order as an argument
        ) { backStackEntry ->
            val order = backStackEntry.arguments?.getInt("order") ?: -1 // Get order from arguments

            DetailScreenRoot() // Pass order to DetailScreenRoot
        }
    }
}