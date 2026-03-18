package com.example.quotes.ui.collection

import com.example.quotes.domain.model.Quote

data class CollectionUiState(
    val collection: List<Quote> = emptyList()
)
