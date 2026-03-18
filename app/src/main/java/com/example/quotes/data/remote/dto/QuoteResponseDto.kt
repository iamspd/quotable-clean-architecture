package com.example.quotes.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuoteResponseDto(
    @SerialName(value = "quotes") val quotes: List<QuoteDto>,
    @SerialName(value = "total") val total: Int,
    @SerialName(value = "skip") val skip: Int,
    @SerialName(value = "limit") val limit: Int
)
