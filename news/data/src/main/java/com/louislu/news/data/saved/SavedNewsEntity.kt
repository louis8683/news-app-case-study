package com.louislu.news.data.saved

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class SavedNewsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    // News fields
    val sourceId: String,
    val sourceName: String,
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: String,
    val content: String
)
