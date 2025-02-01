package com.louislu.favorites.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import com.louislu.core.domain.model.News
import com.louislu.core.presentation.AppBarWithTitle
import com.louislu.core.presentation.ScrollableCards
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
        AppBarWithTitle()

        FavoriteScrollableCards(
            favorites = favoritesFlow,
            onCardClick = { news -> onAction(FavoritesAction.OnNewsCardClick(news)) }
        )

    }
}