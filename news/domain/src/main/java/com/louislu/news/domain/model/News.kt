package com.louislu.news.domain.model

data class News(
    val sourceId: String?,
    val sourceName: String?,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?
)