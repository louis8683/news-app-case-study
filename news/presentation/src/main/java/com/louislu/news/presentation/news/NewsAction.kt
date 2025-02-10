package com.louislu.news.presentation.news

import com.louislu.core.domain.type.NewsCategory
import com.louislu.core.domain.model.News

sealed interface NewsAction {
    data class OnNewsCardClick(val news: News): NewsAction
    data class OnFilterUpdate(val selected: NewsCategory?) : NewsAction
}