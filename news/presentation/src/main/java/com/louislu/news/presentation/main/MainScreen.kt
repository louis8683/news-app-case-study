package com.louislu.news.presentation.main
import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import com.louislu.core.presentation.designsystem.theme.NewsAppCaseStudyTheme
import com.louislu.news.domain.NewsCategory
import com.louislu.news.domain.model.News
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import com.louislu.news.presentation.main.components.AppBarWithTitle
import com.louislu.news.presentation.main.components.BottomNavBar
import com.louislu.news.presentation.main.components.CategoryChipGroup
import com.louislu.news.presentation.main.components.CustomSearchBar
import com.louislu.news.presentation.main.components.FavoriteScrollableCards
import com.louislu.news.presentation.main.components.ScrollableCards
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow


@Composable
fun MainScreenRoot(
    onNewsCardClick: (News) -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    MainScreen(
        state = viewModel.mainState,
        filterState = viewModel.selectedFilter,
        pagedFlow = viewModel.pagedFlow,
        favoritesFlow = viewModel.favorites,
        onAction = { action ->
            when(action) {
                is MainAction.OnNewsCardClick -> {
                    viewModel.onAction(action)
                    onNewsCardClick(action.news)
                }
                is MainAction.OnFilterUpdate -> {
                    viewModel.onAction(action)
                }

                is MainAction.OnSearch -> {
                    viewModel.onAction(action)
                }
                MainAction.OnSearchIconClick -> {
                    viewModel.onAction(action)
                }
                MainAction.OnHomeIconClick -> {
                    viewModel.onAction(action)
                }

                MainAction.OnFavoritesIconClick -> {
                    viewModel.onAction(action)
                }

                MainAction.OnRefresh -> {
                    viewModel.onAction(action)
                }
            }
        },
    )
}

@Composable
fun MainScreen(
    state: MainState,
    filterState: StateFlow<NewsCategory?>,
    pagedFlow: Flow<PagingData<News>>,
    favoritesFlow: Flow<List<News>>,
    onAction: (MainAction) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = { BottomNavBar(
            onNav = { index ->
                when(index) {
                    0 -> {
                        onAction(MainAction.OnHomeIconClick)
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Hello, Snackbar!",
                                actionLabel = "Dismiss",
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                    1 -> {
                        onAction(MainAction.OnSearchIconClick)
                    }
                    2 -> {
                        onAction(MainAction.OnFavoritesIconClick)
                    }
                    else -> throw IllegalStateException(
                        "Unknown index: $index"
                    )
                }
            }
        )}
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AppBarWithTitle()

            if (!state.displayFavorites) {

                if (!state.displaySearch) {
                    CategoryChipGroup(
                        state = state,
                        selectedFilterState = filterState,
                        onFilterUpdate = { selection ->
                            onAction(MainAction.OnFilterUpdate(selection))
                        }
                    )
                } else {
                    CustomSearchBar(
                        onSearch = { search ->
                            onAction(MainAction.OnSearch(search))
                        }
                    )
                }
                ScrollableCards(
                    pagedFlow = pagedFlow,
                    onCardClick = { news ->
                        onAction(MainAction.OnNewsCardClick(news))
                    },
                    onRefresh = { onAction(MainAction.OnRefresh) },
                    snackbarHostState = snackbarHostState,
                    coroutineScope = scope
                )
            }
            else {
                FavoriteScrollableCards(
                    favorites = favoritesFlow,
                    onCardClick = {}
                )
            }
        }

    }
}

@Preview(name = "Light Mode", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun MainScreenPreview() {
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

    NewsAppCaseStudyTheme {
        MainScreen (
            state = MainState(
                displaySearch = false,
                filters = NewsCategory.entries.toList()
            ),
            filterState = MutableStateFlow<NewsCategory?>(null),
            pagedFlow = emptyFlow(),
            onAction = {},
            favoritesFlow = emptyFlow()
        )
    }
}