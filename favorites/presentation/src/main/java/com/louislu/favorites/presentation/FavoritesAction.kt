package com.louislu.favorites.presentation

import com.louislu.core.domain.model.News

sealed interface FavoritesAction {
    data class OnNewsCardClick(val news: News): FavoritesAction
}