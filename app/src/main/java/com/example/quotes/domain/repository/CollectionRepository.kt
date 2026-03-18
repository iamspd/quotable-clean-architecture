package com.example.quotes.domain.repository

import com.example.quotes.domain.model.Quote
import com.example.quotes.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface CollectionRepository {
    suspend fun addQuote(quote: Quote): Result<Unit>

    suspend fun deleteQuote(id: Int): Result<Unit>

    fun getAllQuotes(): Flow<List<Quote>>

    fun getQuoteById(id: Int): Flow<Quote>
}