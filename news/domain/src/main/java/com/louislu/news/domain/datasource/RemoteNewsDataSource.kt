package com.louislu.news.domain.datasource

import com.louislu.core.domain.util.DataError
import com.louislu.core.domain.util.Result
import com.louislu.core.domain.type.NewsCategory
import com.louislu.core.domain.type.NewsSource
import com.louislu.core.domain.model.News

interface RemoteNewsDataSource {

    // Get Everything
    suspend fun getNews(query: String, sources: List<NewsSource>? = null, pageSize: Int? = null, page: Int? = null): Result<List<News>, DataError.Network>

    // Get Headline
    suspend fun getHeadlines(pageSize: Int? = null, page: Int? = null): Result<List<News>, DataError.Network>

    suspend fun getHeadlines(category: NewsCategory, pageSize: Int? = null, page: Int? = null): Result<List<News>, DataError.Network>

    suspend fun getHeadlines(sources: List<NewsSource>, pageSize: Int? = null, page: Int? = null): Result<List<News>, DataError.Network>
}