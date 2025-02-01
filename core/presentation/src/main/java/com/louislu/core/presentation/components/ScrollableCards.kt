package com.louislu.core.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.louislu.core.domain.model.News
import com.louislu.core.presentation.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import timber.log.Timber


@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScrollableCards(
    pagedFlow: Flow<PagingData<News>>,
    onCardClick: (News) -> Unit,
    onRefresh: () -> Unit,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope
) {

    val lazyListState = rememberLazyListState()
    val newsItems = pagedFlow.collectAsLazyPagingItems()
    val isConnected = rememberConnectivityStatus()

    LaunchedEffect(newsItems.itemSnapshotList.items) {
        Timber.i("paged flow updated")
        coroutineScope.launch {
            lazyListState.animateScrollToItem(
                index = 0
            )
        }
    }

    // Check API key on launch
    LaunchedEffect(Unit) {
        if (BuildConfig.API_KEY.isEmpty()) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = "API_KEY is missing!",
                    actionLabel = "Dismiss",
                    duration = SnackbarDuration.Indefinite
                )
            }
        }
    }

    LaunchedEffect(isConnected) {
        Timber.i("Connection: $isConnected")
        if (!isConnected) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = "No Internet",
                    actionLabel = "Dismiss",
                    duration = SnackbarDuration.Short
                )
            }
        } else {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = "Connected to Internet!",
                    actionLabel = "Dismiss",
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    PullToRefreshBox(
        isRefreshing = newsItems.loadState.refresh is LoadState.Loading,
        onRefresh = {
            onRefresh()
            newsItems.refresh()
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = lazyListState
        ) {
            items(
                count = newsItems.itemCount,
                key = { index -> "${newsItems[index]?.title ?: "Unknown"}-$index" },
            ) { index: Int ->

                val news = newsItems[index]
                news?.let {
                    if (index == 0) {
                        CustomCardLarge(
                            news = news,
                            onClick = { onCardClick(news) }
                        )
                    } else {
                        CustomCardSmall(
                            news = news,
                            onClick = {
                                onCardClick(news)
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "Hello, Snackbar!",
                                        actionLabel = "Dismiss",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        )
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "You're all caught up!")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = {
                        coroutineScope.launch {
                            lazyListState.animateScrollToItem(
                                index = 0
                            )
                        }
                    }) {
                        Text(text = "Back to Top")
                    }
                }
            }
        }
    }
}