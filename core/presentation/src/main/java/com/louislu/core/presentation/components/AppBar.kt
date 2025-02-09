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
import com.louislu.core.domain.type.NewsSource
import com.louislu.core.presentation.R
import com.louislu.core.presentation.font.Fonts
import com.louislu.core.presentation.util.imageMap

@Composable
fun AppBar(
    text: String? = null,
    source: NewsSource? = null
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {

        val localImageRes = source?.let { imageMap.getOrDefault(it.apiId, null) }
        localImageRes?.let {
            Image(
                painter = rememberAsyncImagePainter(model = it),
                contentDescription = "Local Image",
                modifier = Modifier
                    .width(168.dp)
                    .height(28.dp)
            )
        } ?: run {
                Text(
                    text = if (!text.isNullOrEmpty()) text else stringResource(id = R.string.app_name),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Fonts.rokkittFontFamily,
                    color = MaterialTheme.colorScheme.onBackground
                )
        }
    }
}


@Preview
@Composable
private fun AppBarWithImagePreview() {
    AppBar(source = NewsSource.WALL_STREET_JOURNAL)
}

@Preview
@Composable
private fun AppBarWithImagePreviewDefault() {
    AppBar()
}