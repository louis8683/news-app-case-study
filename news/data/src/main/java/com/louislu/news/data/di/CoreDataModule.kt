package com.louislu.news.data.di

import com.louislu.news.data.OfflineFirstNewsRepository
import com.louislu.news.data.network.RetrofitRemoteNewsDataSource
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
        applicationScope: CoroutineScope
    ): NewsRepository {
        return OfflineFirstNewsRepository(
            localNewsDatasource = localNewsDataSource,
            remoteNewsDataSource = remoteNewsDataSource,
            applicationScope = applicationScope
        )
    }
}
