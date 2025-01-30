package com.louislu.news.presentation.detail.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.louislu.news.domain.model.News
import java.time.format.DateTimeFormatter
import java.util.Locale


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
                text = news.publishedAt.format(
                    DateTimeFormatter
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