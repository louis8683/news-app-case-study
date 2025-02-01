package com.louislu.favorites.data

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [SavedNewsEntity::class],
    version = 1
)
abstract class SavedDatabase : RoomDatabase() {

    abstract val savedDao: SavedDao
}