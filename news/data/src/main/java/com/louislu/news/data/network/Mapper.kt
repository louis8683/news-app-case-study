package com.louislu.news.data.network

import com.louislu.news.data.network.dto.NewsDto
import com.louislu.core.domain.model.News
import java.time.Instant
import java.time.ZoneId

fun NewsDto.toNews(order: Int): News {
    return News(
        order = order,
        sourceId = source.id ?: "",
        sourceName = source.name,
        author = author ?: "",
        title = title ?: "",
        description = description ?: "",
        url = url ?: "",
        urlToImage = urlToImage ?: "",
        publishedAt = publishedAt.let { Instant
            .parse(publishedAt)
            .atZone(ZoneId.systemDefault()) },
        content = content ?: ""
    )
}