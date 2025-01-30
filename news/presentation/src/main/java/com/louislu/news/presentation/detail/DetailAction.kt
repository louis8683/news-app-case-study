package com.louislu.news.presentation.detail

import com.louislu.news.domain.NewsCategory
import com.louislu.news.domain.model.News

sealed interface DetailAction {
    data object OnBackButtonClick: DetailAction
    data object OnSaveButtonClick: DetailAction
    data object OnLinkButtonClick: DetailAction
}