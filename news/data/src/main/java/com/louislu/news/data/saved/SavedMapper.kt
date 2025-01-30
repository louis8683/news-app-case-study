package com.louislu.news.data.saved

import com.louislu.news.data.room.NewsEntity
import com.louislu.news.domain.model.News
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


fun SavedNewsEntity.toNews(): News {
    return News(
        order = -1,
        sourceId = sourceId,
        sourceName = sourceName,
        author = author,
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        publishedAt = Instant
            .parse(publishedAt)
            .atZone(ZoneId.systemDefault()),
        content = content
    )
}

fun News.toSavedNewsEntity(): SavedNewsEntity {
    return SavedNewsEntity(
        sourceId = sourceId,
        sourceName = sourceName,
        author = author,
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt
            .withZoneSameInstant(ZoneOffset.UTC)
            .format(DateTimeFormatter.ISO_ZONED_DATE_TIME),
        content = content
    )
}
