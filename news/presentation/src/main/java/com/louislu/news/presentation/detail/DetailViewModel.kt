package com.louislu.news.presentation.detail

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.louislu.news.domain.NewsRepository
import com.louislu.news.domain.SavedRepository
import com.louislu.news.domain.model.News
import com.louislu.news.presentation.main.MainAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class DetailViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val savedRepository: SavedRepository,
    private val firebaseAnalytics: FirebaseAnalytics,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val newsOrder: Int = checkNotNull(savedStateHandle["order"])

    private val _news = MutableStateFlow<News?>(null)
    val news: StateFlow<News?> = _news.asStateFlow()

    private val _saved = MutableStateFlow(false)
    val saved: StateFlow<Boolean> = _saved.asStateFlow()

    init {
        fetchNews()
    }

    private fun fetchNews() {
        viewModelScope.launch {
            newsRepository.getNews(newsOrder)
                .collect {
                    _news.value = it

                    _saved.value = savedRepository.isSaved(it.title)
                    Timber.i("saved is currently ${_saved.value}")
                }
        }
    }

    fun onAction(action: DetailAction) {
        when(action) {
            DetailAction.OnBackButtonClick -> TODO()
            DetailAction.OnLinkButtonClick -> TODO()
            DetailAction.OnSaveButtonClick -> {
                if (!_saved.value) {
                    viewModelScope.launch {
                        news.value?.let {
                            savedRepository.save(it)
                            _saved.value = savedRepository.isSaved(it.title)
                        }

                    }
                } else {
                    viewModelScope.launch {
                        news.value?.let {
                            savedRepository.deleteNews(it.title)
                            _saved.value = savedRepository.isSaved(it.title)
                        }
                    }
                }
            }
        }
    }
}