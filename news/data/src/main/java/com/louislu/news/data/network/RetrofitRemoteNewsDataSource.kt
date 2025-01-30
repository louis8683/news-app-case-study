package com.louislu.news.data.network

import com.louislu.core.domain.util.DataError
import com.louislu.core.domain.util.Result
import com.louislu.news.domain.NewsCategory
import com.louislu.news.domain.NewsSource
import com.louislu.news.domain.datasource.RemoteNewsDataSource
import com.louislu.news.domain.model.News
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class RetrofitRemoteNewsDataSource @Inject constructor(
    private val newsApiService: NewsApiService
): RemoteNewsDataSource {

    override suspend fun getNews(
        query: String,
        sources: List<NewsSource>?,
        pageSize: Int?,
        page: Int?
    ): Result<List<News>, DataError.Network> {
        Timber.i("fetching news list from API...")

        try {
            val response = when {
                pageSize != null && page != null && sources != null -> {
                    newsApiService.getEverything(
                        keyword = query,
                        sources = NewsSource.toCommaSeparatedString(sources),
                        pageSize = pageSize,
                        page = page
                    )
                }
                pageSize != null && page != null -> {
                    newsApiService.getEverything(
                        keyword = query,
                        pageSize = pageSize,
                        page = page
                    )
                }
                sources != null -> {
                    newsApiService.getEverything(
                        keyword = query,
                        sources = NewsSource.toCommaSeparatedString(sources),
                    )
                }
                else -> {
                    newsApiService.getEverything(
                        keyword = query,
                    )
                }
            }
            Timber.i("response: ${response.totalResults} results -> ${response.articles.size} articles")
            return Result.Success(response.articles.mapIndexed() { index, newsDto ->  newsDto.toNews(index) })
        } catch (e: HttpException) {
            return httpExceptionToResultError(e)
        }
    }

    override suspend fun getHeadlines(
        pageSize: Int?,
        page: Int?
    ): Result<List<News>, DataError.Network> {
        Timber.i("fetching news list from API...")

        try {
            val response = when {
                pageSize != null && page != null -> {
                    newsApiService.getTopHeadlines(
                        pageSize = pageSize,
                        page = page
                    )
                }
                else -> {
                    newsApiService.getTopHeadlines()
                }
            }
            Timber.i("response: ${response.totalResults} results -> ${response.articles.size} articles")
            return Result.Success(response.articles.mapIndexed() { index, newsDto ->  newsDto.toNews(index) })
        } catch (e: HttpException) {
            return httpExceptionToResultError(e)
        }
    }

    override suspend fun getHeadlines(
        category: NewsCategory,
        pageSize: Int?,
        page: Int?
    ): Result<List<News>, DataError.Network> {
        Timber.i("fetching news list from API...")

        try {
            // Set sources to null because cannot mix source with category
            // (refer to API documentation)
            val response = when {
                pageSize != null && page != null -> {
                    newsApiService.getTopHeadlines(
                        category = category.apiValue,
                        sources = null,
                        pageSize = pageSize,
                        page = page
                    )
                }
                else -> {
                    newsApiService.getTopHeadlines(
                        category = category.apiValue,
                        sources = null
                    )
                }
            }
            Timber.i("response: ${response.totalResults} results -> ${response.articles.size} articles")
            return Result.Success(response.articles.mapIndexed() { index, newsDto ->  newsDto.toNews(index) })
        } catch (e: HttpException) {
            return httpExceptionToResultError(e)
        }
    }

    override suspend fun getHeadlines(
        sources: List<NewsSource>,
        pageSize: Int?,
        page: Int?
    ): Result<List<News>, DataError.Network> {
        Timber.i("fetching news list from API...")

        try {
            val response = when {
                pageSize != null && page != null -> {
                    newsApiService.getTopHeadlines(
                        sources = NewsSource.toCommaSeparatedString(sources),
                        pageSize = pageSize,
                        page = page
                    )
                }
                else -> {
                    newsApiService.getTopHeadlines(
                        sources = NewsSource.toCommaSeparatedString(sources),
                    )
                }
            }
            Timber.i("response: ${response.totalResults} results -> ${response.articles.size} articles")
            return Result.Success(response.articles.mapIndexed() { index, newsDto ->  newsDto.toNews(index) })
        } catch (e: HttpException) {
            return httpExceptionToResultError(e)
        }
    }


    private fun httpExceptionToResultError(e: HttpException): Result.Error<DataError.Network> {
        val error = when (e.code()) {
            400 -> DataError.Network.REQUEST_TIMEOUT  // Bad Request
            401 -> DataError.Network.UNAUTHORIZED     // Unauthorized
            403 -> DataError.Network.UNAUTHORIZED     // Forbidden (similar to 401)
            404 -> DataError.Network.NOT_FOUND        // Not Found
            409 -> DataError.Network.CONFLICT         // Conflict
            413 -> DataError.Network.PAYLOAD_TOO_LARGE // Payload Too Large
            429 -> DataError.Network.TOO_MANY_REQUESTS // Too Many Requests
            in 500..599 -> DataError.Network.SERVER_ERROR // Server Errors
            else -> DataError.Network.UNKNOWN
        }
        Timber.i("HTTP Error ${e.code()}: ${e.message}")
        return Result.Error(error)
    }
}