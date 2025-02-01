package com.louislu.newsappcasestudy

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.louislu.news.presentation.news.NewsScreenRoot
import kotlinx.serialization.Serializable


data class TopLevelRoute<T : Any>(val name: String, val route: T, val icon: ImageVector)

@Serializable data object News
@Serializable data object Search
@Serializable data object Favorites

val topLevelRoutes = listOf(
    TopLevelRoute("News", News, Icons.Default.Home),
    TopLevelRoute("Search", Search, Icons.Default.Search),
    TopLevelRoute("Favorites", Favorites, Icons.Default.Favorite)
)

@Composable
fun MainScreen(
    navigateToDetailWithNews: (com.louislu.news.domain.model.News) -> Unit,
    navigateToDetailWithSaved: (Int) -> Unit
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                topLevelRoutes.forEach { topLevelRoute ->
                    NavigationBarItem(
                        icon = { Icon(imageVector = topLevelRoute.icon, contentDescription = topLevelRoute.name) },
                        label = { Text(text = topLevelRoute.name) },
                        selected = currentDestination?.hierarchy?.any { it.hasRoute(topLevelRoute.route::class) } == true,
                        onClick = {
                          navController.navigate(topLevelRoute.route) {
                              // Pop up to the start destination of the graph to
                              // avoid building up a large stack of destinations
                              // on the back stack as users select items
                              popUpTo(navController.graph.findStartDestination().id) {
                                  saveState = true
                              }
                              // Avoid multiple copies of the same destination when
                              // re-selecting the same item
                              launchSingleTop = true
                              // Restore state when re-selecting a previously selected item
                              restoreState = true
                          }
                        },
                    )
                }
            }
        }
    )  { innerPadding ->
        androidx.navigation.compose.NavHost(
            navController = navController,
            startDestination = News,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<News> { NewsScreenRoot(onNewsCardClick = { news -> navigateToDetailWithNews(news) }) }
            composable<Search> {  }
            composable<Favorites> {  }
        }
    }
}