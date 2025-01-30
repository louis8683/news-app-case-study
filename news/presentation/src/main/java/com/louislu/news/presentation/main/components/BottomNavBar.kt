package com.louislu.news.presentation.main.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue


@Composable
fun BottomNavBar(
    onNav: (Int) -> Unit
) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val items = listOf("News" to Icons.Default.Home, "Search" to Icons.Default.Search, "Favorites" to Icons.Default.Favorite)

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = {
                    selectedIndex = index
                    onNav(index)
                },
                icon = { Icon(imageVector = item.second, contentDescription = item.first) },
                label = { Text(text = item.first) })
        }
    }
}