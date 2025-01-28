package com.louislu.news.presentation.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {

    var mainState by mutableStateOf(MainState())
        private set

    fun onAction(action: MainAction) {

    }

    fun updateFilter(filter: String) {
        Timber.i("Filter updated to $filter")
        mainState = mainState.copy(filter = filter)
    }
}