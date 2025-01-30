package com.louislu.newsappcasestudy

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.louislu.news.presentation.detail.DetailScreenRoot
import com.louislu.news.presentation.main.MainScreenRoot
import com.louislu.newsappcasestudy.parcelable.ParcelableNews
import timber.log.Timber

@Composable
fun NavigationRoot(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        mainGraph(navController)
    }
}

private fun NavGraphBuilder.mainGraph(navController: NavHostController) {
    navigation(
        startDestination = "news",
        route = "main"
    ) {
        composable(route = "news") {
            MainScreenRoot(
                onNewsCardClick = { news ->
                    navController.navigate("detail/${news.order}")
                },
            )
        }
        composable(
            route = "detail/{order}",
            arguments = listOf(navArgument("order") { type = NavType.IntType }) // Pass order as an argument
        ) { backStackEntry ->
            val order = backStackEntry.arguments?.getInt("order") ?: -1 // Get order from arguments

            DetailScreenRoot() // Pass order to DetailScreenRoot
        }
    }
}