package com.louislu.news.presentation.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import com.louislu.core.presentation.font.Fonts
import com.louislu.news.domain.model.News
import com.louislu.news.presentation.R
import com.louislu.news.presentation.main.MainViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

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
            onSaveButtonClick = { onAction(DetailAction.OnSaveButtonClick )},
            saved = saved
        ) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AppBar()
            state.value?.let {
                MainContent(
                    news = it,
                    onLinkButtonClick = { onAction(DetailAction.OnLinkButtonClick) }
                )
            }

        }
    }
}

@Composable
fun AppBar() {
    // TODO: map the news source to the local image of the publisher
    val localImageRes =  R.drawable.wsj_logo

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = localImageRes),
            contentDescription = "Local Image",
            modifier = Modifier
                .width(168.dp)
                .height(28.dp)
        )
    }
}

@Composable
fun MainContent(
    news: News,
    onLinkButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(32.dp, 16.dp)
        ) {
            Text(
                text = news.publishedAt.format(DateTimeFormatter
                    .ofPattern("MMMM d, yyyy h:mm a 'UTC'", Locale.ENGLISH)),
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = news.title,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Author: ${news.author}",
                style = MaterialTheme.typography.bodySmall
            )
        }
        // Online image
        AsyncImage(
            model = news.urlToImage,
            contentDescription = "Online Image",
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
        Column(
            modifier = Modifier
                .padding(32.dp, 16.dp)
                .weight(1f)
        ) {
            Text(
                text = news.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = news.content,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Box(
            modifier = Modifier
                .padding(48.dp, 32.dp)
        ) {
            Button(
                onClick = onLinkButtonClick,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = "Full Article")
            }
        }
    }
}

@Composable
fun CustomBottomBar(
    onReturnButtonClick: () -> Unit,
    onSaveButtonClick: () -> Unit,
    saved: StateFlow<Boolean>
) {

    val state = saved.collectAsState()

    BottomAppBar(
        actions = {
            IconButton(onClick = onReturnButtonClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = onSaveButtonClick) {
                Icon(
                    imageVector = if(state.value) {
                        Icons.Default.Favorite
                    } else {
                        Icons.Default.FavoriteBorder
                    },
                    contentDescription = "Save",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
//        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
//        contentColor = MaterialTheme.colorScheme.onSurface
    )
}

@Preview
@Composable
private fun DetailScreenPreview() {
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

    DetailScreen(news = MutableStateFlow<News>(news), onAction = {}, saved = MutableStateFlow(true))
}