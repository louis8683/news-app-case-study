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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val newsRepository: NewsRepository
): ViewModel() {

    var mainState by mutableStateOf(MainState())
        private set

    private val _selectedFilter = MutableStateFlow<NewsCategory?>(null)

    val selectedFilter: StateFlow<NewsCategory?> = _selectedFilter

    private val _searchQuery = MutableStateFlow("")

    val searchQuery: StateFlow<String> = _searchQuery

    // Store previous values for comparison
    private var previousCategory: NewsCategory? = null
    private var previousQuery: String? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagedFlow: Flow<PagingData<News>> = combine(_selectedFilter, _searchQuery) { category, query ->
        Timber.i("Filter Changed: Query = $query, Category = $category")

        // Determine what changed
        val categoryChanged = previousCategory != category
        val queryChanged = previousQuery != query

        // Update stored values
        previousCategory = category
        previousQuery = query

        Triple(category, query, categoryChanged to queryChanged)
    }
        .flatMapLatest { (category, query, changedFlags) ->
            val (categoryChanged, queryChanged) = changedFlags

            when {
                queryChanged && query.isNotBlank() -> {
                    Timber.i("Fetching news for query: $query")
                    newsRepository.getNewsPaged(query = query)
                }
                categoryChanged -> {
                    Timber.i("Fetching headlines for category: $category")
                    newsRepository.getNewsPaged(category)
                }
                else -> {
                    Timber.i("No changes detected, likely the first fetch")
                    newsRepository.getNewsPaged()
                }
            }
        }
        .cachedIn(viewModelScope)

    // Search functions
    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    init {
        mainState = mainState.copy(
            filters = NewsCategory.entries.toList(),
        )

        viewModelScope.launch {
            mainState = mainState.copy(
                pagedFlow = newsRepository.getNewsPaged().cachedIn(viewModelScope)
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
            MainAction.OnSearchIconClick -> {
                mainState = mainState.copy(displaySearch = true)
            }
            is MainAction.OnSearch -> {
                Timber.i("Searching: ${action.query}")
                _searchQuery.value = action.query

            }
            MainAction.OnHomeIconClick -> {
                mainState = mainState.copy(displaySearch = false)
            }
        }
    }
}