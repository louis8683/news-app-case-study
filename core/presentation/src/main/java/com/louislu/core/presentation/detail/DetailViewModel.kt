package com.louislu.core.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.louislu.core.domain.repository.SavedRepository
import com.louislu.core.domain.model.News
import com.louislu.core.domain.repository.DetailRepository
import com.louislu.core.domain.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class DetailViewModel @Inject constructor(
    private val detailRepository: DetailRepository,
    private val firebaseAnalytics: FirebaseAnalytics,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val newsOrder: Int = checkNotNull(savedStateHandle["order"])
    private val newsTitle: String? = savedStateHandle["title"]

    private val _news = MutableStateFlow<News?>(null)
    val news: StateFlow<News?> = _news

    private val _saved = MutableStateFlow(false)
    val saved: StateFlow<Boolean> = _saved

    init {

        if (newsOrder != -1 && newsTitle != null) {
            throw IllegalStateException("Cannot have both order and title")
        }
        else if (newsOrder != -1) {
            viewModelScope.launch {
                detailRepository.getNewsFromCache(newsOrder)
                    .collect {
                        _news.value = it
                        it?.let { _saved.value = detailRepository.isSaved(it.title) }
                        Timber.i("saved is currently ${_saved.value}")
                    }
            }
        }
        else if (newsTitle != null) {
            viewModelScope.launch {
                detailRepository.getNewsFromSaved(newsTitle)
                    .collect {
                        it?.let { _news.value = it }
                        _saved.value = it?.let { detailRepository.isSaved(it.title) } ?: false
                        Timber.i("saved is currently ${_saved.value}")
                    }
            }
        }
        else {
            throw IllegalStateException("Both order and title are missing")
        }
    }

    fun onAction(action: DetailAction) {
        when(action) {
            DetailAction.OnBackButtonClick -> {}
            DetailAction.OnLinkButtonClick -> {}
            DetailAction.OnSaveButtonClick -> {
                if (!_saved.value) {
                    viewModelScope.launch {
                        news.value?.let {
                            detailRepository.save(it)
                            _saved.value = detailRepository.isSaved(it.title)
                        }

                    }
                } else {
                    viewModelScope.launch {
                        news.value?.let {
                            detailRepository.deleteNews(it.title)
                            _saved.value = detailRepository.isSaved(it.title)
                        }
                    }
                }
            }
        }
    }
}