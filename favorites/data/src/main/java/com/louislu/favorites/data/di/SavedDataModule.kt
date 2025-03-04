package com.louislu.favorites.data.di

import android.content.Context
import androidx.room.Room
import com.louislu.favorites.data.RoomSavedRepository
import com.louislu.favorites.data.SavedDao
import com.louislu.favorites.data.SavedDatabase
import com.louislu.core.domain.repository.SavedRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object SavedDataModule {
    @Provides
    @Singleton
    fun provideSavedDatabase(@ApplicationContext context: Context): SavedDatabase {
        return Room.databaseBuilder(
            context,
            SavedDatabase::class.java,
            "saved.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideSavedDao(database: SavedDatabase): SavedDao {
        return database.savedDao
    }

    @Provides
    @Singleton
    fun provideSavedRepository(savedDao: SavedDao): SavedRepository {
        return RoomSavedRepository(savedDao)
    }

}