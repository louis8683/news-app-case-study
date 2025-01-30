package com.louislu.news.presentation.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.louislu.news.domain.NewsCategory
import com.louislu.news.domain.NewsRepository
import com.louislu.news.domain.model.News
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val newsRepository: NewsRepository
): ViewModel() {

    var mainState by mutableStateOf(MainState())
        private set

    private val _selectedFilter = MutableStateFlow<NewsCategory?>(null)

    val selectedFilter: StateFlow<NewsCategory?> = _selectedFilter

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagedFlow: Flow<PagingData<News>> = _selectedFilter
        .flatMapLatest { category ->
            Timber.i("Re-fetching headlines")
            newsRepository.getHeadlinesPaged(category)
        }
        .cachedIn(viewModelScope)

    init {
        mainState = mainState.copy(
            filters = NewsCategory.entries.toList(),
        )

        viewModelScope.launch {
            mainState = mainState.copy(
                pagedFlow = newsRepository.getHeadlinesPaged().cachedIn(viewModelScope)
            )
        }
    }

    fun onAction(action: MainAction) {
        Timber.i("onAction")
        when(action) {
            is MainAction.OnFilterUpdate -> {
                Timber.i("Filter updated to ${action.selected}")
                _selectedFilter.value = action.selected
            }
            is MainAction.OnNewsCardClick -> {
                Timber.i("News card clicked -> ${action.news.title}")
            }
        }
    }
}