package com.louislu.news.presentation.main.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import com.louislu.news.domain.model.News
import com.louislu.news.presentation.imageMap


@Composable
fun CustomCardLarge(
    news: News,
    onClick: () -> Unit,
) {
    val localImageRes = imageMap.getOrDefault(news.sourceId, null)

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
                localImageRes?.let {
                    Image(
                        painter = rememberAsyncImagePainter(model = localImageRes),
                        contentDescription = "Local Image",
                        modifier = Modifier
                            .height(28.dp)
                    )
                } ?: Text(
                    text = news.sourceName,
                    style = MaterialTheme.typography.titleLarge
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
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}