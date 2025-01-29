package com.louislu.news.presentation.main

import com.louislu.news.domain.NewsCategory
import com.louislu.news.domain.model.News
import java.util.Locale.Category

data class MainState(
    val newsList: List<News> = listOf(),
    val filters: List<NewsCategory> = listOf(),
    val selectedFilter: NewsCategory? = null,
)