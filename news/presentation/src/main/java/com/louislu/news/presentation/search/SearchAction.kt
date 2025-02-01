package com.louislu.news.presentation.search

import com.louislu.core.domain.model.News

sealed interface SearchAction {
    data class OnNewsCardClick(val news: News): SearchAction
    data class OnSearch(val query: String): SearchAction
    data object OnRefresh: SearchAction
}