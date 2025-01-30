package com.louislu.news.domain.datasource

import com.louislu.core.domain.util.DataError
import com.louislu.core.domain.util.Result
import com.louislu.news.domain.model.News
import kotlinx.coroutines.flow.Flow

typealias NewsId = Long

interface LocalNewsDataSource {
    fun getNews(): Flow<List<News>>

    fun getNews(order: Int): Flow<News>

    suspend fun upsertNews(news: News): Result<NewsId, DataError.Local>

    suspend fun upsertNewsList(newsList: List<News>): Result<List<NewsId>, DataError.Local>

    suspend fun deleteNews(id: Long)

    suspend fun deleteAllNews()
}