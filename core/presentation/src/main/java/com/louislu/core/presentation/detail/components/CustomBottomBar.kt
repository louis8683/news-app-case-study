package com.louislu.core.presentation.detail.components

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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.StateFlow

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