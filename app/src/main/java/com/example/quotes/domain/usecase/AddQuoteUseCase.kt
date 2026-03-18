package com.example.quotes.domain.usecase

import com.example.quotes.domain.model.Quote
import com.example.quotes.domain.model.Result
import com.example.quotes.domain.repository.CollectionRepository
import javax.inject.Inject

class AddQuoteUseCase @Inject constructor(
    private val repository: CollectionRepository
) {
    suspend operator fun invoke(quote: Quote): Result<Unit> {

        if (quote.quoteText.isBlank() || quote.authorName.isBlank()) {
            Result.Error(errorMessage = "Fields can not be empty.")
        }
        return repository.addQuote(quote)
    }
}