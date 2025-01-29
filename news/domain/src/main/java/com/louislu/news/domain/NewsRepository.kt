package com.louislu.news.domain

import com.louislu.core.domain.util.DataError
import com.louislu.core.domain.util.EmptyResult
import com.louislu.news.domain.datasource.NewsId
import com.louislu.news.domain.model.News
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getNews(): Flow<List<News>>

    // api fetch related
    suspend fun fetchNews(query: String): EmptyResult<DataError>
    suspend fun fetchHeadlines(category: NewsCategory? = null): EmptyResult<DataError>


    suspend fun upsertNews(news: News): EmptyResult<DataError>
    suspend fun upsertNewsList(newsList: List<News>): EmptyResult<DataError>
    suspend fun deleteNews(id: NewsId)
//    suspend fun syncPendingRuns()
    suspend fun deleteAllNews()
//    suspend fun logout(): EmptyResult<DataError.Network>
}