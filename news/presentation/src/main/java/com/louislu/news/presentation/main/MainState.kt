package com.louislu.news.presentation.main

import com.louislu.news.domain.model.News

data class MainState(
    val newsList: List<News> = listOf(),
    val filter: String = "All",
)