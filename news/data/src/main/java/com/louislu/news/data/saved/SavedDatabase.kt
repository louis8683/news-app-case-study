package com.louislu.news.data.saved

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [SavedNewsEntity::class],
    version = 1
)
abstract class SavedDatabase : RoomDatabase() {

    abstract val savedDao: SavedDao
}