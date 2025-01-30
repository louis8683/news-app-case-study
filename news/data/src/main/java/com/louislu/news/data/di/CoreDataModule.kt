package com.louislu.news.data.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.RemoteMediator
import com.louislu.news.data.OfflineFirstNewsRepository
import com.louislu.news.data.network.NewsApiService
import com.louislu.news.data.network.RetrofitRemoteNewsDataSource
import com.louislu.news.data.paging.NewsRemoteMediator
import com.louislu.news.data.room.NewsDao
import com.louislu.news.data.room.NewsDatabase
import com.louislu.news.data.room.NewsEntity
import com.louislu.news.data.room.RoomLocalNewsDataSource
import com.louislu.news.domain.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object CoreDataModule {

    // Repository
    @Provides
    @Singleton
    fun provideOfflineFirstNewsRepository(
        localNewsDataSource: RoomLocalNewsDataSource,
        remoteNewsDataSource: RetrofitRemoteNewsDataSource,
        newsDatabase: NewsDatabase,
    ): NewsRepository {
        return OfflineFirstNewsRepository(
            localNewsDatasource = localNewsDataSource,
            remoteNewsDataSource = remoteNewsDataSource,
            newsDatabase = newsDatabase,
        )
    }
}
