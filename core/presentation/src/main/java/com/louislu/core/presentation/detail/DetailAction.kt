package com.louislu.core.presentation.detail

sealed interface DetailAction {
    data object OnBackButtonClick: DetailAction
    data object OnSaveButtonClick: DetailAction
    data object OnLinkButtonClick: DetailAction
}