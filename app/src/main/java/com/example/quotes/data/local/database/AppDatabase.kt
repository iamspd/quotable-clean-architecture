package com.example.quotes.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.quotes.data.local.dao.CollectionDao
import com.example.quotes.data.local.entity.CollectionEntity

@Database(entities = [CollectionEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun collectionDao(): CollectionDao
}