package com.example.quotes.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.quotes.data.remote.api.ApiService
import com.example.quotes.domain.model.Quote
import com.example.quotes.domain.repository.QuoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class QuoteRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : QuoteRepository {
    override fun getAllQuotes(): Flow<PagingData<Quote>> {
        return Pager(
            config = PagingConfig(
                pageSize = 30, // should match the API
                prefetchDistance = 5,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                QuotePagingSource(apiService = apiService, limit = 30)
            }
        ).flow
    }
}
