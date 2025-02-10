package com.louislu.core.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.louislu.core.domain.model.News
import com.louislu.core.domain.type.NewsSource
import com.louislu.core.presentation.R
import com.louislu.core.presentation.font.Fonts
import com.louislu.core.presentation.util.imageMap
import timber.log.Timber
import java.time.Instant
import java.time.ZoneId

@Composable
fun AppBar(
    text: String? = null,
    news: News? = null,
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {

        news?.let {
            NewsSource.fromApiId(news.sourceId)?.let { source ->
                imageMap.getOrDefault(source.apiId, null)?.let { localImageRes ->
                    Image(
                        painter = rememberAsyncImagePainter(model = localImageRes),
                        contentDescription = "Local Image",
                        modifier = Modifier
                            .width(168.dp)
                            .height(28.dp)
                    )
                }
            } ?: run {
                Timber.i("Default")
                AppBarCenterText(text = news.sourceName)
            }
        } ?: AppBarCenterText()
    }
}

@Composable
private fun AppBarCenterText(text: String? = null) {
    Text(
        text = if (!text.isNullOrEmpty()) text else stringResource(id = R.string.app_name),
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = Fonts.rokkittFontFamily,
        color = MaterialTheme.colorScheme.onBackground
    )
}


@Preview
@Composable
private fun AppBarWithImagePreview() {
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

    AppBar(news = news)
}

@Preview
@Composable
private fun AppBarWithImagePreviewDefault() {
    AppBar()
}