package com.example.quotes.data.remote.api

import com.example.quotes.data.remote.dto.QuoteResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET(value = "quotes")
    suspend fun getQuoteResponse(
        @Query(value = "limit") limit: Int,
        @Query(value = "skip") skip: Int
    ): QuoteResponseDto
}

