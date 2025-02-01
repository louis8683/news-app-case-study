package com.louislu.core.data

import com.louislu.core.domain.model.News
import com.louislu.core.domain.repository.DetailRepository
import com.louislu.core.domain.repository.NewsRepository
import com.louislu.core.domain.repository.SavedRepository
import com.louislu.core.domain.type.DataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SourceAwareDetailRepository @Inject constructor(
    private val newsRepository: NewsRepository,
    private val savedRepository: SavedRepository
): DetailRepository {

    override suspend fun getNewsFromCache(order: Int): Flow<News?> {
        return newsRepository.getNews(order)
    }

    override suspend fun getNewsFromSaved(title: String): Flow<News?> {
        return savedRepository.getNewsByTitle(title)
    }

    override suspend fun isSaved(title: String): Boolean {
        return savedRepository.isSaved(title)
    }

    override suspend fun save(news: News) {
        return savedRepository.save(news)
    }

    override suspend fun deleteNews(title: String) {
        return savedRepository.deleteNews(title)
    }
}