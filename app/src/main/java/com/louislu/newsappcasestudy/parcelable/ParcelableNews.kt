package com.louislu.newsappcasestudy.parcelable

import android.os.Parcelable
import com.louislu.core.domain.model.News
import kotlinx.parcelize.Parcelize
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


@Parcelize
data class ParcelableNews(
    val order: Int,
    val sourceId: String,
    val sourceName: String,
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAtString: String,  // Store as String for Parcelable
    val content: String
) : Parcelable {
    fun toDomainModel(): News {
        return News(
            order, sourceId, sourceName, author, title, description,
            url, urlToImage,
            ZonedDateTime.parse(publishedAtString, DateTimeFormatter.ISO_ZONED_DATE_TIME),
            content
        )
    }

    companion object {
        fun fromDomainModel(news: News): ParcelableNews {
            return ParcelableNews(
                news.order, news.sourceId, news.sourceName, news.author, news.title, news.description,
                news.url, news.urlToImage,
                news.publishedAt.format(DateTimeFormatter.ISO_ZONED_DATE_TIME),
                news.content
            )
        }
    }
}