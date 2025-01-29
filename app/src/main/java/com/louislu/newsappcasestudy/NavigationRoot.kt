package com.louislu.newsappcasestudy

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.louislu.news.presentation.main.MainScreenRoot

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
                onNewsCardClick = { /* TODO */ },
            )
        }
    }
}