package com.louislu.news.presentation.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
        // TODO: remove this
//        val news = News(
//            sourceId = "bloomberg",
//            sourceName = "Bloomberg",
//            author = "Stephanie Lai, Josh Wingrove",
//            title = "Trump Says Microsoft Eyeing TikTok Bid With App’s Future in US Unclear - Bloomberg",
//            description = "Microsoft Corp. is in talks to acquire the US arm of ByteDance Ltd.’s TikTok, President Donald Trump said Monday night, without elaborating.",
//            url = "https://www.bloomberg.com/news/articles/2025-01-28/trump-says-microsoft-eyeing-tiktok-bid-with-app-s-future-unclear",
//            urlToImage = "https://assets.bwbx.io/images/users/iqjWHBFdfxIU/iAUVtbQTpnv8/v1/1200x800.jpg",
//            publishedAt = Instant.parse("2025-01-28T02:44:00Z").atZone(ZoneId.systemDefault()),
//            content = "Microsoft Corp. is in talks to acquire the US arm of ByteDance Ltd.s TikTok, President Donald Trump said Monday night, without elaborating.\\r\\nI would say yes, Trump told reporters aboard Air Force One… [+124 chars]"
//        )
//
//        mainState = mainState.copy(
//            newsList = listOf(news, news, news, news, news, news, news, news, news)
//        )


        // TODO: refine the logic
        Timber.i("Init")

        newsRepository.testPing()

        viewModelScope.launch {
            newsRepository.getNews()
                .catch { e ->
                    // TODO: Handle the error
                    Timber.i("Error in getNews()")
                }
                .collect { newsList ->
                    Timber.i("Recieved news, size: ${newsList.size}")
                    mainState = mainState.copy(newsList = newsList)
                }
        }

        viewModelScope.launch {
            Timber.i("fetching...")
            newsRepository.fetchNews()
            Timber.i("finished fetching")
        }
    }

    fun onAction(action: MainAction) {
        when(action) {
            is MainAction.OnFilterUpdate -> {
                Timber.i("Filter updated to ${action.filter}")
                mainState = mainState.copy(filter = action.filter)
            }
            MainAction.OnNewsCardClick -> TODO()
        }
    }
}