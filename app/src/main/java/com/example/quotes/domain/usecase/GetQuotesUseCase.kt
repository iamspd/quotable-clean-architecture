package com.example.quotes.domain.usecase

import androidx.paging.PagingData
import com.example.quotes.domain.model.Quote
import com.example.quotes.domain.repository.QuoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetQuotesUseCase @Inject constructor(
    private val quoteRepository: QuoteRepository
) {
    operator fun invoke(): Flow<PagingData<Quote>> {
        return quoteRepository.getAllQuotes()
    }
}
