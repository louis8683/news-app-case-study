package com.louislu.favorites.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.louislu.core.presentation.analytics.AnalyticsManager
import com.louislu.core.domain.enumclass.NewsCategory
import com.louislu.core.domain.repository.SavedRepository
import com.louislu.core.domain.model.News
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
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