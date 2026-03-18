package com.example.quotes.data.repository

import androidx.paging.PagingSource
import com.example.quotes.data.remote.api.ApiService
import com.example.quotes.data.remote.dto.QuoteDto
import com.example.quotes.data.remote.dto.QuoteResponseDto
import com.example.quotes.domain.model.Quote
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class QuotePagingSourceTest {

    private lateinit var apiService: ApiService
    private lateinit var quotePagingSource: QuotePagingSource

    @Before
    fun setUp() {
        apiService = mockk()
        quotePagingSource = QuotePagingSource(apiService = apiService, limit = 30)
    }

    @Test
    fun quotePagingSource_load_returnsSuccessfulLoad() = runTest {
        val mockResponseDto = QuoteResponseDto(
            quotes = List(30) { index ->
                QuoteDto(id = index, quoteText = "Test $index", authorName = "Developer")
            },
            total = 100,
            skip = 0,
            limit = 30
        )
        coEvery { apiService.getQuoteResponse(limit = 30, skip = 0) } returns mockResponseDto

        val loadParams = PagingSource.LoadParams.Refresh(
            key = 0,
            loadSize = 30,
            placeholdersEnabled = false
        )
        val result = quotePagingSource.load(loadParams)
        assertTrue(result is PagingSource.LoadResult.Page)

        val page = result as PagingSource.LoadResult.Page<Int, Quote>
        assertEquals(30, page.data.size)
        assertEquals("Test 0", page.data.first().quoteText)

        assertEquals(null, page.prevKey)
        assertEquals(30, page.nextKey)
    }

    @Test
    fun quotePagingSource_load_returnsAnError() = runTest {
        val errorResponse = Response.error<QuoteResponseDto>(
            500,
            "".toResponseBody(null)
        )
        coEvery { apiService.getQuoteResponse(any(), any()) } throws HttpException(errorResponse)

        val loadParams = PagingSource.LoadParams.Refresh(
            key = 0,
            loadSize = 30,
            placeholdersEnabled = false
        )
        val result = quotePagingSource.load(loadParams)

        assertTrue(result is PagingSource.LoadResult.Error)
        val error = result as PagingSource.LoadResult.Error
        assertTrue(error.throwable is HttpException)
        assertEquals(500, (error.throwable as HttpException).code())
    }
}
