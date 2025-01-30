package com.louislu.news.domain

import com.louislu.news.domain.model.News
import kotlinx.coroutines.flow.Flow


interface SavedRepository {

    suspend fun getSaved(): Flow<List<News>>

    suspend fun save(news: News)

    suspend fun deleteNews(title: String)

    suspend fun isSaved(title: String): Boolean
}