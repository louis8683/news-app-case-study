package com.louislu.core.domain.type

enum class NewsCategory(val apiValue: String) {
    BUSINESS("business"),
    ENTERTAINMENT("entertainment"),
    GENERAL("general"),
    HEALTH("health"),
    SCIENCE("science"),
    SPORTS("sports"),
    TECHNOLOGY("technology");

    companion object {
        fun fromApiValue(value: String): NewsCategory? {
            return entries.find { it.apiValue.equals(value, ignoreCase = true) }
        }
    }
}