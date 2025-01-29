package com.louislu.news.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class NewsEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0L,

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
