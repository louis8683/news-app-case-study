package com.louislu.core.domain.type

sealed interface DataSource {
    data object News : DataSource
    data object Favorites : DataSource
}