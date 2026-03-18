package com.example.quotes.data.mappers

import com.example.quotes.data.local.entity.CollectionEntity
import com.example.quotes.domain.model.Quote

fun CollectionEntity.toQuote() =
    Quote(
        id = id,
        quoteText = quoteText,
        authorName = authorName
    )

fun Quote.toCollectionEntity() =
    CollectionEntity(
        id = id,
        quoteText = quoteText,
        authorName = authorName
    )