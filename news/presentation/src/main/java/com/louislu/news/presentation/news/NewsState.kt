package com.louislu.news.presentation.news

import com.louislu.core.domain.enumclass.NewsCategory


data class NewsState(
    val filters: List<NewsCategory> = listOf(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false
)