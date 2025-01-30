package com.louislu.news.data.saved

import com.louislu.news.data.room.NewsDao
import com.louislu.news.domain.SavedRepository
import com.louislu.news.domain.model.News
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject


class RoomSavedRepository @Inject constructor(
    private val savedDao: SavedDao
): SavedRepository {
    override fun getSaved(): Flow<List<News>> {
        Timber.i("getSaved()")
        return savedDao.getSavedNews().map { list -> list.map { it.toNews() } }
    }

    override suspend fun save(news: News) {
        Timber.i("save(${news.title})")
        return savedDao.upsertNews(news.toSavedNewsEntity())
    }

    override suspend fun deleteNews(title: String) {
        Timber.i("deleteNews($title)")
        savedDao.deleteNewsByTitle(title)
    }

    override suspend fun isSaved(title: String): Boolean {
        Timber.i("isSaved($title)")
        return savedDao.isNewsSaved(title) > 0
    }

}