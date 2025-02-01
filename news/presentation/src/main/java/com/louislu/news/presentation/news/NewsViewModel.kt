package com.louislu.news.presentation.news

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.louislu.core.presentation.analytics.AnalyticsManager
import com.louislu.core.domain.enumclass.NewsCategory
import com.louislu.news.domain.NewsRepository
import com.louislu.core.domain.model.News
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val analyticsManager: AnalyticsManager
): ViewModel() {

    var newsState by mutableStateOf(NewsState())
        private set

    private val _selectedFilter = MutableStateFlow<NewsCategory?>(null)
    val selectedFilter: StateFlow<NewsCategory?> = _selectedFilter


    @OptIn(ExperimentalCoroutinesApi::class)
    val pagedFlow: Flow<PagingData<News>> = _selectedFilter
        .flatMapLatest { category ->
            Timber.i("Re-fetching headlines")
            newsRepository.getNewsPaged(category)
        }
        .cachedIn(viewModelScope)

    init {
        newsState = newsState.copy(
            filters = NewsCategory.entries.toList(),
        )
    }

    fun onAction(action: NewsAction) {
        Timber.i("onAction")
        when(action) {
            is NewsAction.OnFilterUpdate -> {
                Timber.i("Filter updated to ${action.selected}")
                _selectedFilter.value = action.selected
                analyticsManager.logFilterSelected(action.selected.toString())
            }
            is NewsAction.OnNewsCardClick -> {
                Timber.i("News card clicked -> ${action.news.title}")
                analyticsManager.logNewsClicked(action.news.title)
            }
            NewsAction.OnRefresh -> {
                Timber.i("Refreshing...")
                // TODO
            }
        }
    }
}