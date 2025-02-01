package com.louislu.news.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.louislu.core.presentation.analytics.AnalyticsManager
import com.louislu.core.domain.repository.NewsRepository
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
class SearchViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val analyticsManager: AnalyticsManager
): ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagedFlow: Flow<PagingData<News>> = _searchQuery
        .flatMapLatest { query ->
            newsRepository.getNewsPaged(query = query)
        }
        .cachedIn(viewModelScope)


    fun onAction(action: SearchAction) {
        Timber.i("onAction")
        when(action) {
            is SearchAction.OnNewsCardClick -> {
                Timber.i("News card clicked -> ${action.news.title}")
                analyticsManager.logNewsClicked(action.news.title)
            }
            is SearchAction.OnSearch -> {
                Timber.i("Searching: ${action.query}")
                _searchQuery.value = action.query
                analyticsManager.logSearch(action.query)
            }

            SearchAction.OnRefresh -> {
                Timber.i("Refreshing...")
            }
        }
    }
}