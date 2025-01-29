package com.louislu.news.data

import com.louislu.core.domain.util.DataError
import com.louislu.core.domain.util.EmptyResult
import com.louislu.core.domain.util.Result
import com.louislu.core.domain.util.asEmptyDataResult
import com.louislu.news.data.network.RetrofitRemoteNewsDataSource
import com.louislu.news.data.room.RoomLocalNewsDataSource
import com.louislu.news.domain.NewsCategory
import com.louislu.news.domain.datasource.NewsId
import com.louislu.news.domain.model.News
import com.louislu.news.domain.NewsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import timber.log.Timber


class OfflineFirstNewsRepository(
    private val localNewsDatasource: RoomLocalNewsDataSource,
    private val remoteNewsDataSource: RetrofitRemoteNewsDataSource,
    private val applicationScope: CoroutineScope
): NewsRepository {

    override fun getNews(): Flow<List<News>> {
        // Single source of truth (local database)
        return localNewsDatasource.getNews()
    }

    override suspend fun fetchNews(query: String): EmptyResult<DataError> {
        Timber.i("fetching news...")
        return when(val result = remoteNewsDataSource.getNews(query = query)) {
            is Result.Error -> {
                Timber.i("fetch error")
                result.asEmptyDataResult()
            }
            is Result.Success -> {
                Timber.i("fetch success")
                // Issue: coroutine cancellation, ex., viewModelScope cancellation
                // Solution #1 (discouraged): withContext(NonCancellable)
                // Solution #2: use the application scope
                // Note: why "async" then "await"? Because this line of code will
                //       live on even if "fetchNews" suspend function is cancelled
                applicationScope.async {
                    localNewsDatasource.deleteAllNews()
                    localNewsDatasource.upsertNewsList(result.data).asEmptyDataResult()
                }.await()
            }
        }
    }

    override suspend fun fetchHeadlines(category: NewsCategory?): EmptyResult<DataError> {
        Timber.i("fetching headlines...")
        val result =
            if (category != null)
                remoteNewsDataSource.getHeadlines(category = category)
            else
                remoteNewsDataSource.getHeadlines()

        return when(result) {
            is Result.Error -> {
                Timber.i("fetch error")
                result.asEmptyDataResult()
            }
            is Result.Success -> {
                Timber.i("fetch success")
                applicationScope.async {
                    localNewsDatasource.deleteAllNews()
                    localNewsDatasource.upsertNewsList(result.data).asEmptyDataResult()
                }.await()
            }
        }
    }

    override suspend fun upsertNews(news: News): EmptyResult<DataError> {
        TODO("Not yet implemented")
    }

    override suspend fun upsertNewsList(newsList: List<News>): EmptyResult<DataError> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteNews(id: NewsId) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllNews() {
        localNewsDatasource.deleteAllNews()
    }
}