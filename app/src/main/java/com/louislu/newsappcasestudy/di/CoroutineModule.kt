package com.louislu.newsappcasestudy.di

import android.app.Application
import com.louislu.newsappcasestudy.NewsAppCaseStudyApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object CoroutineModule {

    @Provides
    @Singleton
    fun provideApplicationScope(application: Application): CoroutineScope {
        return (application as NewsAppCaseStudyApplication).applicationScope
    }
}