package com.louislu.favorites.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.louislu.core.domain.model.News
import com.louislu.core.presentation.components.AppBar
import com.louislu.favorites.presentation.components.FavoriteScrollableCards
import kotlinx.coroutines.flow.Flow


@Composable
fun FavoritesScreenRoot(
    onNewsCardClick: (News) -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    MainScreen(
        favoritesFlow = viewModel.favorites,
        onAction = { action ->
            when(action) {
                is FavoritesAction.OnNewsCardClick -> {
                    viewModel.onAction(action)
                    onNewsCardClick(action.news)
                }
            }
        },
    )
}

@Composable
fun MainScreen(
    favoritesFlow: Flow<List<News>>,
    onAction: (FavoritesAction) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AppBar()

        FavoriteScrollableCards(
            favorites = favoritesFlow,
            onCardClick = { news -> onAction(FavoritesAction.OnNewsCardClick(news)) }
        )

    }
}