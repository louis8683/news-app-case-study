package com.louislu.news.data.network.dto

import kotlinx.serialization.Serializable


@Serializable
data class NewsResponseDto(
    val status: String,
    val totalResults: Int,
    val articles: List<NewsDto>
)
