package com.example.quotes.domain.usecase

import com.example.quotes.domain.model.Quote
import com.example.quotes.domain.repository.CollectionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetQuoteByIdUseCase @Inject constructor(
    private val repository: CollectionRepository
) {
    operator fun invoke(id: Int): Flow<Quote> {
        return repository.getQuoteById(id)
    }
}