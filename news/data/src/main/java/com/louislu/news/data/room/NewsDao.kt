package com.louislu.news.data.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow


@Dao
interface NewsDao {

    @Upsert
    suspend fun upsertNews(news: NewsEntity)

    @Upsert
    suspend fun upsertNewsList(newsList: List<NewsEntity>)

    @Query("SELECT * FROM newsentity ORDER BY publishedAt DESC")
    fun getNews(): Flow<List<NewsEntity>>

    @Query("DELETE FROM newsentity WHERE id=:id")
    suspend fun deleteNews(id: Long)

    @Query("DELETE FROM newsentity")
    suspend fun deleteAllNews()
}