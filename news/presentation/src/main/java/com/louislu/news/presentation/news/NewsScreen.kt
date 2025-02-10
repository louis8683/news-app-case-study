package com.louislu.news.presentation.news

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import com.louislu.core.domain.type.NewsCategory
import com.louislu.core.domain.model.News
import com.louislu.core.presentation.components.AppBar
import com.louislu.core.presentation.components.ScrollableCards
import com.louislu.news.presentation.news.components.CategoryChipGroup
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import java.time.Instant
import java.time.ZoneId

@Composable
fun NewsScreenRoot(
    onNewsCardClick: (News) -> Unit,
    viewModel: NewsViewModel = hiltViewModel()
) {
    val filters by viewModel.filters
    val selectedFilter by viewModel.selectedFilter.collectAsState()

    NewsScreen(
        filters = filters,
        selectedFilter = selectedFilter,
        pagedFlow = viewModel.pagedFlow,
        onAction = { action ->
            when(action) {
                is NewsAction.OnFilterUpdate -> viewModel.onAction(action)
                is NewsAction.OnNewsCardClick -> {
                    viewModel.onAction(action)
                    onNewsCardClick(action.news)
                }
            }

        }
    )
}

@Composable
fun NewsScreen(
    filters: List<NewsCategory>,
    selectedFilter: NewsCategory?,
    pagedFlow: Flow<PagingData<News>>,
    onAction: (NewsAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AppBar()
        CategoryChipGroup(
            filters = filters,
            selectedFilter = selectedFilter,
            onFilterUpdate = { selection ->
                onAction(NewsAction.OnFilterUpdate(selection))
            }
        )
        ScrollableCards(
            pagedFlow = pagedFlow,
            onCardClick = { news ->
                onAction(NewsAction.OnNewsCardClick(news))
            },
        )
    }
}


@Preview
@Composable
private fun NewsScreenPreviewAll() {

    val news = News(
        order = 1,
        sourceId = "bloomberg",
        sourceName = "Bloomberg",
        author = "Stephanie Lai, Josh Wingrove",
        title = "Trump Says Microsoft Eyeing TikTok Bid With App’s Future in US Unclear - Bloomberg",
        description = "Microsoft Corp. is in talks to acquire the US arm of ByteDance Ltd.’s TikTok, President Donald Trump said Monday night, without elaborating.",
        url = "https://www.bloomberg.com/news/articles/2025-01-28/trump-says-microsoft-eyeing-tiktok-bid-with-app-s-future-unclear",
        urlToImage = "https://assets.bwbx.io/images/users/iqjWHBFdfxIU/iAUVtbQTpnv8/v1/1200x800.jpg",
        publishedAt = Instant.parse("2025-01-28T02:44:00Z").atZone(ZoneId.of("UTC")),
        content = "Microsoft Corp. is in talks to acquire the US arm of ByteDance Ltd.s TikTok, President Donald Trump said Monday night, without elaborating.\\r\\nI would say yes, Trump told reporters aboard Air Force One… [+124 chars]"
    )

    val pagedFlow = flowOf(PagingData.from(listOf(news, news, news, news, news)))

    NewsScreen(
        filters = NewsCategory.entries.toList(),
        selectedFilter = null,
        pagedFlow = pagedFlow,
        onAction = {}
    )
}


@Preview
@Composable
private fun NewsScreenPreviewBusiness() {
    val news = News(
        order = 1,
        sourceId = "bloomberg",
        sourceName = "Bloomberg",
        author = "Stephanie Lai, Josh Wingrove",
        title = "Trump Says Microsoft Eyeing TikTok Bid With App’s Future in US Unclear - Bloomberg",
        description = "Microsoft Corp. is in talks to acquire the US arm of ByteDance Ltd.’s TikTok, President Donald Trump said Monday night, without elaborating.",
        url = "https://www.bloomberg.com/news/articles/2025-01-28/trump-says-microsoft-eyeing-tiktok-bid-with-app-s-future-unclear",
        urlToImage = "https://assets.bwbx.io/images/users/iqjWHBFdfxIU/iAUVtbQTpnv8/v1/1200x800.jpg",
        publishedAt = Instant.parse("2025-01-28T02:44:00Z").atZone(ZoneId.of("UTC")),
        content = "Microsoft Corp. is in talks to acquire the US arm of ByteDance Ltd.s TikTok, President Donald Trump said Monday night, without elaborating.\\r\\nI would say yes, Trump told reporters aboard Air Force One… [+124 chars]"
    )

    val pagedFlow = flowOf(PagingData.from(listOf(news, news, news, news, news)))

    NewsScreen(
        filters = NewsCategory.entries.toList(),
        selectedFilter = NewsCategory.BUSINESS,
        pagedFlow = pagedFlow,
        onAction = {}
    )
}