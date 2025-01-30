package com.louislu.news.presentation.main

import androidx.paging.PagingData
import com.louislu.news.domain.NewsCategory
import com.louislu.news.domain.model.News
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import java.util.Locale.Category

data class MainState(
//    val newsList: List<News> = listOf(),
    val filters: List<NewsCategory> = listOf(),
    val isLoading: Boolean = false,
    val currentPage: Int = 0,
    val hasMorePages: Boolean = true,
    val pagedFlow: Flow<PagingData<News>> = emptyFlow(),
    val displaySearch: Boolean = false,
    val isSearchActive: Boolean = false,
    val displayFavorites: Boolean = false,
    val isRefreshing: Boolean = false
)