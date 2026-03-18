package com.example.quotes.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "collection")
data class CollectionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val quoteText: String,
    val authorName: String
)
