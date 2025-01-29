package com.louislu.news.data.network

import com.louislu.core.domain.util.DataError
import com.louislu.core.domain.util.Result
import com.louislu.news.domain.datasource.RemoteNewsDataSource
import com.louislu.news.domain.model.News
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class RetrofitRemoteNewsDataSource @Inject constructor(
    private val newsApiService: NewsApiService
): RemoteNewsDataSource {

    override suspend fun getNewsList(): Result<List<News>, DataError.Network> {
        Timber.i("fetching news list from API...")

        try {
            val response = newsApiService.getEverything("bitcoin")
            Timber.i("response: ${response.totalResults} results -> ${response.articles.size} articles")
            return Result.Success(response.articles.map { it.toNews() })
        } catch (e: HttpException) {
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

    override suspend fun getNews(query: String): Result<List<News>, DataError.Network> {
        TODO("Not yet implemented")
    }
}