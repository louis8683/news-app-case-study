package com.louislu.core.presentation.detail

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.louislu.core.domain.model.News
import com.louislu.core.presentation.detail.components.AppBarWithImage
import com.louislu.core.presentation.detail.components.CustomBottomBar
import com.louislu.core.presentation.detail.components.MainContent
import com.louislu.core.presentation.util.imageMap
import com.louislu.core.presentation.components.AppBarWithTitle
import kotlinx.coroutines.flow.StateFlow


@Composable
fun DetailScreenRoot(
    viewModel: DetailViewModel = hiltViewModel()
) {
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val context = LocalContext.current

    DetailScreen(
        news = viewModel.news,
        saved = viewModel.saved,
        onAction = { action ->
            when(action) {
                DetailAction.OnBackButtonClick -> {
                    backDispatcher?.onBackPressed()
                }
                DetailAction.OnLinkButtonClick -> {
                    viewModel.news.value?.let {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.url))
                        context.startActivity(intent)
                    }
                }
                DetailAction.OnSaveButtonClick -> {
                    viewModel.onAction(action)
                }
            }
        }
    )
}


@Composable
fun DetailScreen(
    news: StateFlow<News?>,
    saved: StateFlow<Boolean>,
    onAction: (DetailAction) -> Unit
) {
    val state = news.collectAsState()

    Scaffold(
        bottomBar = { CustomBottomBar(
            onReturnButtonClick = { onAction(DetailAction.OnBackButtonClick) },
            onSaveButtonClick = { onAction(DetailAction.OnSaveButtonClick)},
            saved = saved
        ) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {


            state.value?.let { news ->
                val localImageRes = imageMap.getOrDefault(news.sourceId, null)
                localImageRes?.let {
                    AppBarWithImage(localImageRes)
                }
            } ?: AppBarWithTitle()
            state.value?.let {
                MainContent(
                    news = it,
                    onLinkButtonClick = { onAction(DetailAction.OnLinkButtonClick) }
                )
            }

        }
    }
}


//@Preview
//@Composable
//private fun DetailScreenPreview() {
//    val news = News(
//        order = 1,
//        sourceId = "bloomberg",
//        sourceName = "Bloomberg",
//        author = "Stephanie Lai, Josh Wingrove",
//        title = "Trump Says Microsoft Eyeing TikTok Bid With App’s Future in US Unclear - Bloomberg",
//        description = "Microsoft Corp. is in talks to acquire the US arm of ByteDance Ltd.’s TikTok, President Donald Trump said Monday night, without elaborating.",
//        url = "https://www.bloomberg.com/news/articles/2025-01-28/trump-says-microsoft-eyeing-tiktok-bid-with-app-s-future-unclear",
//        urlToImage = "https://assets.bwbx.io/images/users/iqjWHBFdfxIU/iAUVtbQTpnv8/v1/1200x800.jpg",
//        publishedAt = Instant.parse("2025-01-28T02:44:00Z").atZone(ZoneId.of("UTC")),
//        content = "Microsoft Corp. is in talks to acquire the US arm of ByteDance Ltd.s TikTok, President Donald Trump said Monday night, without elaborating.\\r\\nI would say yes, Trump told reporters aboard Air Force One… [+124 chars]"
//    )
//
//    DetailScreen(news = MutableStateFlow<News>(news), onAction = {}, saved = MutableStateFlow(true))
//}