package com.louislu.news.presentation.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import com.louislu.core.domain.model.News
import com.louislu.core.presentation.components.AppBar
import com.louislu.news.presentation.search.components.CustomSearchBar
import com.louislu.core.presentation.components.ScrollableCards
import kotlinx.coroutines.flow.Flow


@Composable
fun SearchScreenRoot(
    onNewsCardClick: (News) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    SearchScreen(
        pagedFlow = viewModel.pagedFlow,
        onAction = { action ->
            when(action) {
                is SearchAction.OnNewsCardClick -> {
                    viewModel.onAction(action)
                    onNewsCardClick(action.news)
                }
                is SearchAction.OnSearch -> {
                    viewModel.onAction(action)
                }
                SearchAction.OnRefresh -> {
                    viewModel.onAction(action)
                }
            }
        },
    )
}

@Composable
fun SearchScreen(
    pagedFlow: Flow<PagingData<News>>,
    onAction: (SearchAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AppBar()
        CustomSearchBar(
            onSearch = { search ->
                onAction(SearchAction.OnSearch(search))
            }
        )
        ScrollableCards(
            pagedFlow = pagedFlow,
            onCardClick = { news ->
                onAction(SearchAction.OnNewsCardClick(news))
            }
        )
    }
}