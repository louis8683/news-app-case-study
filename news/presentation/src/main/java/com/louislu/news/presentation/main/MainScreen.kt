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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
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
import androidx.compose.ui.graphics.Color
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
import com.louislu.news.presentation.R


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
            ScrollableCards()
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
fun ScrollableCards() {
    var cardItems = (1..10).map { "Card #$it" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        cardItems.forEachIndexed { index, cardText ->
            if (index == 0) {
                CustomCardLarge(
                    onlineImageUrl = "https://static01.nyt.com/images/2025/01/27/multimedia/27DEEPSEEK-EXPLAINER-1-01-hpmc/27DEEPSEEK-EXPLAINER-1-01-hpmc-superJumbo.jpg?quality=75&auto=webp",
                    localImageRes = R.drawable.wsj_logo,
                    title = "Trump says he will impose 25% tariffs on Colombia in showdown over deportation flights",
                    onClick = {}
                )
            }
            else {
                CustomCardSmall(
                    onlineImageUrl = "https://static01.nyt.com/images/2025/01/27/multimedia/27DEEPSEEK-EXPLAINER-1-01-hpmc/27DEEPSEEK-EXPLAINER-1-01-hpmc-superJumbo.jpg?quality=75&auto=webp",
                    localImageRes = R.drawable.wsj_logo,
                    title = "Trump says he will impose 25% tariffs on Colombia in showdown over deportation flights",
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
    onlineImageUrl: String,
    localImageRes: Int,
    title: String,
    onClick: () -> Unit,
) {
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
                model = onlineImageUrl,
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
                    text = title,
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
    onlineImageUrl: String,
    localImageRes: Int,
    title: String,
    onClick: () -> Unit,
) {
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
                    text = title,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                )
            }

            // Online image
            AsyncImage(
                model = onlineImageUrl,
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
    NewsAppCaseStudyTheme {
        MainScreen (
            state = MainState("All"),
            onAction = {},
            filters = listOf("All", "Headlines", "Politics", "Tech", "Climate", "Sports", "Lifestyle", "World", "Business")
        )
    }
}