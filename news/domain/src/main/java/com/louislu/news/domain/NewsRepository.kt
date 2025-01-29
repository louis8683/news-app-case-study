package com.louislu.news.domain

import com.louislu.core.domain.util.DataError
import com.louislu.core.domain.util.EmptyResult
import com.louislu.news.domain.datasource.NewsId
import com.louislu.news.domain.model.News
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getNews(): Flow<List<News>>
    suspend fun fetchNews(): EmptyResult<DataError>
    suspend fun upsertNews(news: News): EmptyResult<DataError>
    suspend fun upsertNewsList(newsList: List<News>): EmptyResult<DataError>
    suspend fun deleteNews(id: NewsId)
//    suspend fun syncPendingRuns()
    suspend fun deleteAllRuns()
//    suspend fun logout(): EmptyResult<DataError.Network>

    // TODO: remove this test ping function
    fun testPing(): Unit
}