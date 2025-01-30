package com.louislu.news.presentation.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import com.louislu.core.presentation.designsystem.theme.NewsAppCaseStudyTheme
import com.louislu.core.presentation.font.Fonts
import com.louislu.news.domain.NewsCategory
import com.louislu.news.domain.model.News
import com.louislu.news.presentation.R
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.Instant
import java.time.ZoneId
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.louislu.news.presentation.BuildConfig
import com.louislu.news.presentation.imageMap
import com.louislu.news.presentation.main.components.AppBarWithTitle
import com.louislu.news.presentation.main.components.BottomNavBar
import com.louislu.news.presentation.main.components.CategoryChipGroup
import com.louislu.news.presentation.main.components.CustomCardLarge
import com.louislu.news.presentation.main.components.CustomCardSmall
import com.louislu.news.presentation.main.components.CustomSearchBar
import com.louislu.news.presentation.main.components.FavoriteScrollableCards
import com.louislu.news.presentation.main.components.ScrollableCards
import com.louislu.news.presentation.main.components.rememberConnectivityStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import java.util.Locale.Category


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