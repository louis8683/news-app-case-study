package com.louislu.news.presentation.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
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
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import com.louislu.core.presentation.designsystem.theme.NewsAppCaseStudyTheme
import com.louislu.core.presentation.font.Fonts
import com.louislu.news.domain.model.News
import com.louislu.news.presentation.R
import java.time.Instant
import java.time.ZoneId


@Composable
fun MainScreenRoot(
    onNewsCardClick: () -> Unit,
    filters: List<String>,
    viewModel: MainViewModel = hiltViewModel()
) {
    MainScreen(
        state = viewModel.mainState,
        onAction = { action ->
            when(action) {
                MainAction.OnNewsCardClick -> onNewsCardClick()
                is MainAction.OnFilterUpdate -> {
                    viewModel.onAction(action)
                }
            }
        },
        filters = filters
    )
}

@Composable
fun MainScreen(
    state: MainState,
    onAction: (MainAction) -> Unit,
    filters: List<String>
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
            ChipGroup(
                state = state,
                chipLabels = filters,
                onFilterUpdate = { selection ->
                    onAction(MainAction.OnFilterUpdate(selection))
                }
            )
            ScrollableCards(state = state)
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
fun ChipGroup(
    state: MainState,
    chipLabels: List<String>,
    onFilterUpdate: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        chipLabels.forEach { chip ->
            FilterChip(
                selected = state.filter == chip,
                onClick = {
                    onFilterUpdate(chip)
                },
                label = { Text(text = chip) },
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}

@Composable
fun ScrollableCards(
    state: MainState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        state.newsList.forEachIndexed { index, news ->
            if (index == 0) {
                CustomCardLarge(
                    news = news,
                    onClick = {}
                )
            }
            else {
                CustomCardSmall(
                    news = news,
                    onClick = {}
                )
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
                listOf(news, news, news),
                "All"
            ),
            onAction = {},
            filters = listOf("All", "Headlines", "Politics", "Tech", "Climate", "Sports", "Lifestyle", "World", "Business")
        )
    }
}