package com.louislu.news.presentation.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.louislu.news.domain.NewsCategory
import com.louislu.news.domain.NewsRepository
import com.louislu.news.domain.model.News
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
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

    init {
        // Setup the filter
        mainState = mainState.copy(
            filters = NewsCategory.entries.toList(),
            selectedFilter = null
        )

        viewModelScope.launch {
            newsRepository.getNews()
                .catch { e ->
                    // TODO: Handle the error
                    Timber.i("Error in getNews()")
                }
                .collect { newsList ->
                    Timber.i("Received news, size: ${newsList.size}")

                    newsList.take(10).forEachIndexed { index, news ->
                        Timber.i("News #$index -> ${news.title}")
                    }

                    mainState = mainState.copy(newsList = newsList)
                }
        }

        fetchHeadlines()
    }

    fun onAction(action: MainAction) {
        when(action) {
            is MainAction.OnFilterUpdate -> {
                Timber.i("Filter updated to ${action.selected}")

                mainState = mainState.copy(selectedFilter = action.selected)
                fetchHeadlines(action.selected)
            }
            MainAction.OnNewsCardClick -> TODO()
        }
    }

    private fun fetchHeadlines(category: NewsCategory? = null) {
        viewModelScope.launch {
            Timber.i("fetching...")
            newsRepository.fetchHeadlines(category)
            Timber.i("finished fetching")
        }
    }
}