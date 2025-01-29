package com.louislu.news.data.di

import android.content.Context
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.louislu.news.data.OfflineFirstNewsRepository
import com.louislu.news.data.network.Constants
import com.louislu.news.data.network.NewsApiService
import com.louislu.news.data.network.RetrofitRemoteNewsDataSource
import com.louislu.news.data.room.NewsDao
import com.louislu.news.data.room.NewsDatabase
import com.louislu.news.data.room.RoomLocalNewsDataSource
import com.louislu.news.domain.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
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
