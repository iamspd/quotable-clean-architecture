package com.example.quotes.domain.repository

import androidx.paging.PagingData
import com.example.quotes.domain.model.Quote
import kotlinx.coroutines.flow.Flow

interface QuoteRepository {
    fun getAllQuotes(): Flow<PagingData<Quote>>
}