package com.louislu.news.data.di

import com.louislu.news.data.OfflineFirstNewsRepository
import com.louislu.news.data.network.RetrofitRemoteNewsDataSource
import com.louislu.news.data.room.NewsDatabase
import com.louislu.news.data.room.RoomLocalNewsDataSource
import com.louislu.core.domain.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NewsDataModule {

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
