package com.louislu.news.presentation.main

sealed interface MainAction {
    data object OnNewsCardClick: MainAction
    data class OnFilterUpdate(val filter: String) : MainAction
}