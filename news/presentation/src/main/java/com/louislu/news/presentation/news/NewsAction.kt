package com.louislu.news.presentation.news

import com.louislu.core.domain.enumclass.NewsCategory
import com.louislu.core.domain.model.News

sealed interface NewsAction {
    data class OnNewsCardClick(val news: News): NewsAction
    data class OnFilterUpdate(val selected: NewsCategory?) : NewsAction
    data object OnRefresh: NewsAction
}