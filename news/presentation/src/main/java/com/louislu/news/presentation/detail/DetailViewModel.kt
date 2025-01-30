package com.louislu.news.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.louislu.news.domain.NewsRepository
import com.louislu.news.domain.model.News
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val newsOrder: Int = checkNotNull(savedStateHandle["order"])

    private val _news = MutableStateFlow<News?>(null)
    val news: StateFlow<News?> = _news

    init {
        viewModelScope.launch {
            newsRepository.getNews(newsOrder)
                .collect { _news.value = it }
        }
    }
}