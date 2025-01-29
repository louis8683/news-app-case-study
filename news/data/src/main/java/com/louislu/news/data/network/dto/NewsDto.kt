package com.louislu.news.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class NewsDto(
    val source: SourceDto,
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?
)