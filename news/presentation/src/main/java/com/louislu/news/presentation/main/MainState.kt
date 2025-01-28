package com.louislu.news.presentation.main

import androidx.compose.foundation.text.input.TextFieldState
import com.louislu.news.domain.model.News

data class MainState(
    val newList: List<News> = listOf(),
    val filter: String = "All",
)