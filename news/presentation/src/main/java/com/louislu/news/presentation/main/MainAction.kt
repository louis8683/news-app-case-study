package com.louislu.news.presentation.main

import com.louislu.news.domain.NewsCategory
import com.louislu.news.domain.model.News

sealed interface MainAction {
    data class OnNewsCardClick(val news: News): MainAction
    data class OnFilterUpdate(val selected: NewsCategory?) : MainAction

}