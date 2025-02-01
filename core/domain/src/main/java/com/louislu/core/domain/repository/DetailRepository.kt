package com.louislu.core.domain.repository

import com.louislu.core.domain.model.News
import com.louislu.core.domain.type.DataSource
import kotlinx.coroutines.flow.Flow


interface DetailRepository {

    suspend fun getNewsFromCache(order: Int): Flow<News?>

    suspend fun getNewsFromSaved(title: String): Flow<News?>

    suspend fun isSaved(title: String): Boolean

    suspend fun save(news: News)

    suspend fun deleteNews(title: String)

}