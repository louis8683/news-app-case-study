package com.louislu.news.data.network

import com.louislu.news.data.BuildConfig
import com.louislu.news.data.network.dto.NewsResponseDto
import com.louislu.core.domain.type.NewsSource
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    // the "everything" endpoint
    @GET("everything")
    suspend fun getEverything(
        @Query("q") keyword: String,
        @Query("sources") sources: String? = NewsSource.ALL_SOURCES,
        @Query("language") language: String? = "en",
        @Query("sortBy") sortBy: String? = "popularity",
        @Query("pageSize") pageSize: Int = 100,
        @Query("page") page: Int = 1,
        @Query("apiKey") apikey: String = BuildConfig.API_KEY
    ): NewsResponseDto

    // the "top-headlines" endpoint
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String? = "us",
        @Query("category") category: String? = null,
        @Query("sources") sources: String? = null,
        @Query("q") keyword: String? = null,
        @Query("pageSize") pageSize: Int = 100,
        @Query("page") page: Int = 1,
        @Query("apiKey") apikey: String = BuildConfig.API_KEY
    ): NewsResponseDto
}

// API endpoint: {{base_url}}/everything?q=bitcoin&apiKey={{api_key}}
