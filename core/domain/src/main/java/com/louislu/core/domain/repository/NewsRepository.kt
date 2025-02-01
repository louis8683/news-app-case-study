package com.louislu.core.domain.repository

import androidx.paging.PagingData
import com.louislu.core.domain.model.News
import com.louislu.core.domain.type.NewsCategory
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
//    fun getNews(): Flow<List<News>>

    // api fetch related
//    suspend fun fetchNews(query: String): EmptyResult<DataError>
//    suspend fun fetchHeadlines(category: NewsCategory? = null): EmptyResult<DataError>

    suspend fun getNewsPaged(category: NewsCategory? = null, query: String? = null): Flow<PagingData<News>>
    suspend fun getNews(order: Int): Flow<News?>

//    suspend fun upsertNews(news: News): EmptyResult<DataError>
//    suspend fun upsertNewsList(newsList: List<News>): EmptyResult<DataError>
//    suspend fun deleteNews(id: NewsId)
//    suspend fun syncPendingRuns()
    suspend fun deleteAllNews()
//    suspend fun logout(): EmptyResult<DataError.Network>
}