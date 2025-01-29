package com.louislu.news.domain.datasource

import com.louislu.core.domain.util.DataError
import com.louislu.core.domain.util.Result
import com.louislu.news.domain.model.News

interface RemoteNewsDataSource {

    suspend fun getNewsList(): Result<List<News>, DataError.Network>

    suspend fun getNews(query: String): Result<List<News>, DataError.Network>
}