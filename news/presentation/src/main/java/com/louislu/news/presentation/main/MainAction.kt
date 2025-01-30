package com.louislu.news.presentation.main

import com.louislu.news.domain.NewsCategory
import com.louislu.news.domain.model.News

sealed interface MainAction {
    data class OnNewsCardClick(val news: News): MainAction
    data class OnFilterUpdate(val selected: NewsCategory?) : MainAction
    data object OnHomeIconClick: MainAction
    data object OnSearchIconClick: MainAction
    data class OnSearch(val query: String): MainAction
}