package com.example.quotes.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuoteDto(
    @SerialName(value = "id") val id: Int,
    @SerialName(value = "quote") val quoteText: String = "Unknown",
    @SerialName(value = "author") val authorName: String = "Unknown"
)
