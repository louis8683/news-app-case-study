package com.louislu.core.domain.model

import java.time.ZonedDateTime

data class News(
    val order: Int,
    val sourceId: String,
    val sourceName: String,
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: ZonedDateTime,
    val content: String
)