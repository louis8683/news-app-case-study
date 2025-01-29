package com.louislu.news.data.room

import android.database.sqlite.SQLiteFullException
import com.louislu.core.domain.util.DataError
import com.louislu.core.domain.util.Result
import com.louislu.news.domain.datasource.LocalNewsDataSource
import com.louislu.news.domain.datasource.NewsId
import com.louislu.news.domain.model.News
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomLocalNewsDataSource @Inject constructor(
    private val newsDao: NewsDao
): LocalNewsDataSource {
    override fun getNews(): Flow<List<News>> {
        return newsDao.getNews().map { entities ->
            entities.map { it.toNews() }
        }
    }

    override suspend fun upsertNews(news: News): Result<NewsId, DataError.Local> {
        return try {
            val entity = news.toNewsEntity()
            newsDao.upsertNews(entity)
            Result.Success(entity.id)
        } catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun upsertNewsList(newsList: List<News>): Result<List<NewsId>, DataError.Local> {
        return try {
            val entities = newsList.map { it.toNewsEntity() }
            newsDao.upsertNewsList(entities)
            Result.Success(entities.map { it.id })
        } catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun deleteNews(id: Long) {
        newsDao.deleteNews(id)
    }

    override suspend fun deleteAllNews() {
        newsDao.deleteAllNews()
    }
}