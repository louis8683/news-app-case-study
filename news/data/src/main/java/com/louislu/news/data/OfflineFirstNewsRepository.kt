package com.louislu.news.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.louislu.core.domain.util.DataError
import com.louislu.core.domain.util.EmptyResult
import com.louislu.core.domain.util.Result
import com.louislu.core.domain.util.asEmptyDataResult
import com.louislu.news.data.network.NewsApiService
import com.louislu.news.data.network.RetrofitRemoteNewsDataSource
import com.louislu.news.data.paging.NewsRemoteMediator
import com.louislu.news.data.room.NewsDatabase
import com.louislu.news.data.room.NewsEntity
import com.louislu.news.data.room.RoomLocalNewsDataSource
import com.louislu.news.data.room.toNews
import com.louislu.news.domain.NewsCategory
import com.louislu.news.domain.datasource.NewsId
import com.louislu.news.domain.model.News
import com.louislu.news.domain.NewsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber


class OfflineFirstNewsRepository(
    private val localNewsDatasource: RoomLocalNewsDataSource,
    private val remoteNewsDataSource: RetrofitRemoteNewsDataSource,
    private val newsDatabase: NewsDatabase,
): NewsRepository {

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getHeadlinesPaged(category: NewsCategory?): Flow<PagingData<News>> {

        Timber.i("getHeadlinesPaged")

        // Test code for pager
        val pager = Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = NewsRemoteMediator(newsDatabase, remoteNewsDataSource, NewsRemoteMediator.Companion.Endpoint.TOP_HEADLINES, category = category)) {
            newsDatabase.newsDao.pagingSource()
        }

        return pager.flow.map { pagingData: PagingData<NewsEntity> ->
            pagingData.map { it.toNews() }
        }
    }

    override suspend fun deleteAllNews() {
        localNewsDatasource.deleteAllNews()
    }
}