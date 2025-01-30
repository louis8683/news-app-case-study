package com.louislu.news.presentation.main

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import java.util.Locale.Category


@Composable
fun MainScreenRoot(
    onNewsCardClick: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    MainScreen(
        state = viewModel.mainState,
        filterState = viewModel.selectedFilter,
        pagedFlow = viewModel.pagedFlow,
        onAction = { action ->
            when(action) {
                is MainAction.OnNewsCardClick -> {
                    viewModel.onAction(action)
                    onNewsCardClick()
                }
                is MainAction.OnFilterUpdate -> {
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
    onAction: (MainAction) -> Unit,
) {
    Scaffold(
        bottomBar = { BottomNavBar() }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AppBar()
            CategoryChipGroup(
                state = state,
                selectedFilterState = filterState,
                onFilterUpdate = { selection ->
                    onAction(MainAction.OnFilterUpdate(selection))
                }
            )
            ScrollableCards(
                pagedFlow = pagedFlow,
                onCardClick = { news ->
                    onAction(MainAction.OnNewsCardClick(news))
                }
            )
        }

    }
}

@Composable
fun AppBar() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(MaterialTheme.colorScheme.onPrimary),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Case Study",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = Fonts.rokkittFontFamily,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun CategoryChipGroup(
    state: MainState,
    selectedFilterState: StateFlow<NewsCategory?>,
    onFilterUpdate: (NewsCategory?) -> Unit
) {
    val categories = listOf(null) + state.filters
    val selectedFilter by selectedFilterState.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { category ->
            FilterChip(
                selected = selectedFilter == category,
                onClick = {
                    onFilterUpdate(category)
                },
                label = { Text(text = category?.displayName() ?: "All") },
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}

@Composable
fun ScrollableCards(
    pagedFlow: Flow<PagingData<News>>,
    onCardClick: (News) -> Unit
) {

    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val newsItems = pagedFlow.collectAsLazyPagingItems()

    LaunchedEffect(newsItems.itemSnapshotList.items) {
        Timber.i("paged flow updated")
        coroutineScope.launch {
            lazyListState.animateScrollToItem(
                index = 0
            )
        }
    }

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
                }
                else {
                    CustomCardSmall(
                        news = news,
                        onClick = { onCardClick(news) }
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

@Composable
fun BottomNavBar() {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val items = listOf("News" to Icons.Default.Home, "Search" to Icons.Default.Search, "Favorites" to Icons.Default.Favorite)

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = { selectedIndex = index },
                icon = { Icon(imageVector = item.second, contentDescription = item.first) },
                label = { Text(text = item.first) })
        }
    }
}

@Composable
fun CustomCardLarge(
    news: News,
    onClick: () -> Unit,
) {
    // TODO: map the news source to the local image of the publisher
    val localImageRes =  R.drawable.wsj_logo

    Card(
        shape = RoundedCornerShape(12.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // Online image
            AsyncImage(
                model = news.urlToImage,
                contentDescription = "Online Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            // Local Image
            Box(
                modifier = Modifier
                    .padding(8.dp, 4.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = localImageRes),
                    contentDescription = "Local Image",
                    modifier = Modifier
                        .width(168.dp)
                        .height(28.dp)
                )
            }

            // Text
            Box(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Text(
                    text = news.title.ifEmpty { "Title not available" },
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun CustomCardSmall(
    news: News,
    onClick: () -> Unit,
) {
    // TODO: map the news source to the local image of the publisher
    val localImageRes =  R.drawable.wsj_logo

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
            ) {
                // Local Image
                Image(
                    painter = rememberAsyncImagePainter(model = localImageRes),
                    contentDescription = "Local Image",
                    modifier = Modifier
                        .width(168.dp)
                        .height(28.dp)
                )

                // Text
                Text(
                    text = news.title.ifEmpty { "Title not available" },
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                )
            }

            // Online image
            AsyncImage(
                model = news.urlToImage,
                contentDescription = "Online Image",
                modifier = Modifier
                    .fillMaxHeight()
                    .width(100.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Preview
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
                filters = NewsCategory.entries.toList()
            ),
            filterState = MutableStateFlow<NewsCategory?>(null),
            pagedFlow = emptyFlow(),
            onAction = {},
        )
    }
}