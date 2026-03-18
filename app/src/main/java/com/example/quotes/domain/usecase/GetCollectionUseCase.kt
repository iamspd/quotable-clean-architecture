package com.example.quotes.domain.usecase

import com.example.quotes.domain.model.Quote
import com.example.quotes.domain.repository.CollectionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCollectionUseCase @Inject constructor(
    private val repository: CollectionRepository
) {
    operator fun invoke(): Flow<List<Quote>> {
        return repository.getAllQuotes()
    }
}