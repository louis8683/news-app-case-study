package com.louislu.core.domain.repository

import com.louislu.core.domain.model.News
import kotlinx.coroutines.flow.Flow


interface SavedRepository {

    fun getSaved(): Flow<List<News>>

    suspend fun save(news: News)

    suspend fun deleteNews(title: String)

    suspend fun isSaved(title: String): Boolean
}