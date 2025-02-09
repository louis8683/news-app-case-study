package com.louislu.news.presentation.news

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import com.louislu.core.domain.type.NewsCategory
import com.louislu.core.domain.model.News
import com.louislu.core.presentation.components.AppBar
import com.louislu.core.presentation.components.ScrollableCards
import com.louislu.news.presentation.news.components.CategoryChipGroup
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun NewsScreenRoot(
    onNewsCardClick: (News) -> Unit,
    viewModel: NewsViewModel = hiltViewModel()
) {
    NewsScreen(
        state = viewModel.newsState,
        filterState = viewModel.selectedFilter,
        pagedFlow = viewModel.pagedFlow,
        onAction = { action ->
            when(action) {
                is NewsAction.OnFilterUpdate -> viewModel.onAction(action)
                is NewsAction.OnNewsCardClick -> {
                    viewModel.onAction(action)
                    onNewsCardClick(action.news)
                }
                NewsAction.OnRefresh -> viewModel.onAction(action)
            }

        }
    )
}

@Composable
fun NewsScreen(
    state: NewsState,
    filterState: StateFlow<NewsCategory?>,
    pagedFlow: Flow<PagingData<News>>,
    onAction: (NewsAction) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AppBar()
        CategoryChipGroup(
            state = state,
            selectedFilterState = filterState,
            onFilterUpdate = { selection ->
                onAction(NewsAction.OnFilterUpdate(selection))
            }
        )
        ScrollableCards(
            pagedFlow = pagedFlow,
            onCardClick = { news ->
                onAction(NewsAction.OnNewsCardClick(news))
            },
            onRefresh = { onAction(NewsAction.OnRefresh) },
            snackbarHostState = snackbarHostState,
            coroutineScope = scope
        )
    }
}