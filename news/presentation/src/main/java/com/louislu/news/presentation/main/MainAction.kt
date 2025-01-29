package com.louislu.news.presentation.main

import com.louislu.news.domain.NewsCategory

sealed interface MainAction {
    data object OnNewsCardClick: MainAction
    data class OnFilterUpdate(val selected: NewsCategory?) : MainAction
}