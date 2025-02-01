package com.louislu.news.presentation.news.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.louislu.core.domain.enumclass.NewsCategory
import com.louislu.core.presentation.displayName
import com.louislu.news.presentation.news.NewsState
import kotlinx.coroutines.flow.StateFlow


@Composable
fun CategoryChipGroup(
    state: NewsState,
    selectedFilterState: StateFlow<NewsCategory?>,
    onFilterUpdate: (NewsCategory?) -> Unit
) {
    val categories = listOf(null) + state.filters
    val selectedFilter by selectedFilterState.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .horizontalScroll(rememberScrollState())
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
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