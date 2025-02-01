package com.louislu.core.presentation

import com.louislu.core.domain.enumclass.NewsCategory


fun NewsCategory.displayName(): String {
    return displayNames[this] ?: this.name.lowercase().replaceFirstChar { it.uppercase() } // Fallback
}

private val displayNames = mapOf(
    NewsCategory.BUSINESS to "Business",
    NewsCategory.ENTERTAINMENT to "Entertainment",
    NewsCategory.GENERAL to "General",
    NewsCategory.HEALTH to "Health",
    NewsCategory.SCIENCE to "Science",
    NewsCategory.SPORTS to "Sports",
    NewsCategory.TECHNOLOGY to "Technology"
)