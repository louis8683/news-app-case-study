package com.louislu.news.presentation.main

import com.louislu.core.domain.type.NewsCategory
import com.louislu.core.domain.model.News

sealed interface MainAction {
    data class OnNewsCardClick(val news: News): MainAction
    data class OnFilterUpdate(val selected: NewsCategory?) : MainAction
    data object OnHomeIconClick: MainAction
    data object OnSearchIconClick: MainAction
    data object OnFavoritesIconClick: MainAction
    data class OnSearch(val query: String): MainAction
    data object OnRefresh: MainAction
}