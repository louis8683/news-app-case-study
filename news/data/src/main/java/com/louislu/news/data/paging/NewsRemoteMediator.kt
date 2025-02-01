package com.louislu.news.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.louislu.core.domain.util.Result
import com.louislu.news.data.room.NewsDatabase
import com.louislu.news.data.room.NewsEntity
import com.louislu.news.data.room.toNewsEntity
import com.louislu.core.domain.type.NewsCategory
import com.louislu.news.domain.datasource.RemoteNewsDataSource
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException


@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediator(
    private val newsDatabase: NewsDatabase,
    private val remoteNewsDataSource: RemoteNewsDataSource,
    private val endpoint: Endpoint,
    private val query: String? = null,
    private val category: NewsCategory? = null
): RemoteMediator<Int, NewsEntity>() {

    companion object {
        enum class Endpoint {
            EVERYTHING,
            TOP_HEADLINES
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, NewsEntity>
    ): MediatorResult {
        Timber.i("Running load...")

        return try {
            val loadKey = when(loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        1
                    } else {
                        lastItem.fetchOrder / state.config.pageSize + 2
                    }
                }
            }

            Timber.i("Last load item: ${state.lastItemOrNull()}")
            Timber.i("Last load item: ${state.lastItemOrNull()}")
            Timber.i("Next load key: $loadKey")

            val result = when(endpoint) {
                Endpoint.EVERYTHING -> {
                    query?.let { remoteNewsDataSource.getNews(
                        it,
                        pageSize = state.config.pageSize,
                        page = loadKey
                    )
                    } ?: throw IllegalArgumentException()
                }
                Endpoint.TOP_HEADLINES -> {
                    if (category == null) {
                        remoteNewsDataSource.getHeadlines(
                            pageSize = state.config.pageSize,
                            page = loadKey
                        )
                    } else {
                        remoteNewsDataSource.getHeadlines(
                            category=category,
                            pageSize = state.config.pageSize,
                            page = loadKey
                        )
                    }
                }
            }

            if (result is Result.Error) {
                // TODO: throw a better error
                Timber.i("Result is an error")
                MediatorResult.Error(IllegalStateException())
            }
            else {
                val newsList = (result as Result.Success).data.mapIndexed { index, news ->
                    val order = (loadKey - 1) * state.config.pageSize + index
                    news.copy(order = order).toNewsEntity()
                }

                newsDatabase.withTransaction {
                    // TODO: examine the logic here
                    if (loadType == LoadType.REFRESH || loadKey == 1) {
                        newsDatabase.newsDao.deleteAllNews()
                    }

                    newsDatabase.newsDao.upsertNewsList(newsList)
                }

                Timber.i("Result is an Success")
                MediatorResult.Success(
                    endOfPaginationReached = newsList.isEmpty()
                )
            }

        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}