package com.example.quotes.domain.usecase

import com.example.quotes.domain.model.Result
import com.example.quotes.domain.repository.CollectionRepository
import javax.inject.Inject

class DeleteQuoteByIdUseCase @Inject constructor(
    private val repository: CollectionRepository
) {
    suspend operator fun invoke(id: Int): Result<Unit> {
        return repository.deleteQuote(id)
    }
}