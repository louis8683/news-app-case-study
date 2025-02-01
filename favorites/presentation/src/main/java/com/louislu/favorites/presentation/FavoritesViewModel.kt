package com.louislu.favorites.presentation

import androidx.lifecycle.ViewModel
import com.louislu.core.presentation.analytics.AnalyticsManager
import com.louislu.core.domain.repository.SavedRepository
import com.louislu.core.domain.model.News
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val savedRepository: SavedRepository,
    private val analyticsManager: AnalyticsManager
): ViewModel() {

    // Favorites
    private val _favorites = savedRepository.getSaved()
    val favorites: Flow<List<News>> = _favorites

    fun onAction(action: FavoritesAction) {
        Timber.i("onAction")
        when(action) {
            is FavoritesAction.OnNewsCardClick -> {
                Timber.i("News card clicked -> ${action.news.title}")
                analyticsManager.logNewsClicked(action.news.title)
            }
        }
    }
}