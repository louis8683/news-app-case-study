package com.louislu.favorites.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.louislu.core.presentation.analytics.AnalyticsManager
import com.louislu.core.domain.repository.SavedRepository
import com.louislu.core.domain.model.News
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val savedRepository: SavedRepository,
    private val analyticsManager: AnalyticsManager
): ViewModel() {

    private val _favorites = MutableStateFlow<List<News>>(emptyList())
    val favorites: StateFlow<List<News>> = _favorites

    init {
        viewModelScope.launch {
            savedRepository.getSaved().collect {
                _favorites.value = it
            }
        }
    }

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