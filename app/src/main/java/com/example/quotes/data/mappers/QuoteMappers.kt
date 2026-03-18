package com.example.quotes.data.mappers

import com.example.quotes.data.remote.dto.QuoteDto
import com.example.quotes.domain.model.Quote

fun QuoteDto.toQuote() =
    Quote(
        id = id,
        quoteText = quoteText,
        authorName = authorName
    )