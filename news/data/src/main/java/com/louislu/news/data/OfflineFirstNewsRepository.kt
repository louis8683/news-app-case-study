package com.louislu.news.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.louislu.news.data.network.RetrofitRemoteNewsDataSource
import com.louislu.news.data.paging.NewsRemoteMediator
import com.louislu.news.data.room.NewsDatabase
import com.louislu.news.data.room.NewsEntity
import com.louislu.news.data.room.RoomLocalNewsDataSource
import com.louislu.news.data.room.toNews
import com.louislu.news.domain.NewsCategory
import com.louislu.news.domain.model.News
import com.louislu.news.domain.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber


class OfflineFirstNewsRepository(
    private val localNewsDatasource: RoomLocalNewsDataSource,
    private val remoteNewsDataSource: RetrofitRemoteNewsDataSource,
    private val newsDatabase: NewsDatabase,
): NewsRepository {

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getNewsPaged(category: NewsCategory?, query: String?): Flow<PagingData<News>> {

        Timber.i("getPaged")

        val pager = if (category != null) {
            Pager(
                config = PagingConfig(pageSize = 10),
                remoteMediator = NewsRemoteMediator(
                    newsDatabase,
                    remoteNewsDataSource,
                    NewsRemoteMediator.Companion.Endpoint.TOP_HEADLINES,
                    category = category
                )
            ) {
                newsDatabase.newsDao.pagingSource()
            }
        } else if (query != null) {
            Pager(
                config = PagingConfig(pageSize = 10),
                remoteMediator = NewsRemoteMediator(
                    newsDatabase,
                    remoteNewsDataSource,
                    NewsRemoteMediator.Companion.Endpoint.EVERYTHING,
                    query = query
                )
            ) {
                newsDatabase.newsDao.pagingSource()
            }
        } else {
            Pager(
                config = PagingConfig(pageSize = 10),
                remoteMediator = NewsRemoteMediator(
                    newsDatabase,
                    remoteNewsDataSource,
                    NewsRemoteMediator.Companion.Endpoint.TOP_HEADLINES
                )
            ) {
                newsDatabase.newsDao.pagingSource()
            }
        }

        return pager.flow.map { pagingData: PagingData<NewsEntity> ->
            pagingData.map { it.toNews() }
        }
    }

    override suspend fun getNews(order: Int): Flow<News> {
        return localNewsDatasource.getNews(order)
    }

    override suspend fun deleteAllNews() {
        localNewsDatasource.deleteAllNews()
    }
}