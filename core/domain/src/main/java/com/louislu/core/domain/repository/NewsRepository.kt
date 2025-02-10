package com.louislu.core.domain.repository

import androidx.paging.PagingData
import com.louislu.core.domain.model.News
import com.louislu.core.domain.type.NewsCategory
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    suspend fun getNewsPaged(category: NewsCategory? = null, query: String? = null): Flow<PagingData<News>>
    suspend fun getNews(order: Int): Flow<News?>

    suspend fun deleteAllNews()
}