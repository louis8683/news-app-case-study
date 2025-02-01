package com.louislu.core.data.di

import com.louislu.core.data.SourceAwareDetailRepository
import com.louislu.core.domain.repository.DetailRepository
import com.louislu.core.domain.repository.NewsRepository
import com.louislu.core.domain.repository.SavedRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object SavedDataModule {

    @Provides
    @Singleton
    fun provideDetailRepository(
        newsRepository: NewsRepository,
        savedRepository: SavedRepository
    ): DetailRepository {
        return SourceAwareDetailRepository(
            newsRepository,
            savedRepository
        )
    }

}