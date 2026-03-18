package com.example.quotes.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.quotes.data.local.entity.CollectionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionDao {

    @Upsert
    suspend fun addQuote(quote: CollectionEntity)

    @Query(value = "DELETE FROM collection WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query(value = "SELECT * FROM collection")
    fun getAllQuotes(): Flow<List<CollectionEntity>>

    @Query(value = "SELECT * FROM collection WHERE id = :id")
    fun getQuoteById(id: Int): Flow<CollectionEntity>
}
