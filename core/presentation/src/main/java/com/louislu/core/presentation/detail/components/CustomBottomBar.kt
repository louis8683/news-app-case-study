package com.louislu.core.presentation.detail.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.StateFlow

@Composable
fun CustomBottomBar(
    onReturnButtonClick: () -> Unit,
    onSaveButtonClick: () -> Unit,
    saved: Boolean
) {

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
                    imageVector = if(saved) {
                        Icons.Default.Favorite
                    } else {
                        Icons.Default.FavoriteBorder
                    },
                    contentDescription = "Save",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    )
}


@Preview
@Composable
private fun CustomBottomBarPreviewOff() {
    CustomBottomBar(onReturnButtonClick = {}, onSaveButtonClick = {}, saved = false)
}


@Preview
@Composable
private fun CustomBottomBarPreviewOn() {
    CustomBottomBar(onReturnButtonClick = {}, onSaveButtonClick = {}, saved = true)
}
