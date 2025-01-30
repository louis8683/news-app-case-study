package com.louislu.news.data.saved

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow


@Dao
interface SavedDao {
    @Upsert
    suspend fun upsertNews(news: SavedNewsEntity) // Save news locally

    @Query("SELECT * FROM savednewsentity")
    fun getSavedNews(): Flow<List<SavedNewsEntity>> // Get saved articles

    @Query("SELECT COUNT(*) FROM savednewsentity WHERE title = :title")
    suspend fun isNewsSaved(title: String): Int // Check if a news article is saved

    @Query("DELETE FROM SavedNewsEntity WHERE title = :title")
    suspend fun deleteNewsByTitle(title: String) // Remove a saved news by title
}