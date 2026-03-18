package com.example.quotes.data.remote.api

import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class ApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: ApiService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
        val contentType = "application/json".toMediaType()

        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(ApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun apiService_getQuoteResponse_JSONParsesSuccessfully() = runTest {
        val mockJson = """
            {
                "quotes": [
                    {
                        "id": 1,
                        "quote": "A test quote.",
                        "author": "Developer"
                    }
                ],
                "total": 1454,
                "skip": 0,
                "limit": 1
            }
        """.trimIndent()

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(mockJson)
        mockWebServer.enqueue(mockResponse)

        val actualResponse = apiService.getQuoteResponse(limit = 1, skip = 0)

        assertEquals(1454, actualResponse.total)
        assertEquals(1, actualResponse.quotes.size)

        val firstQuote = actualResponse.quotes.first()
        assertEquals(1, firstQuote.id)
        assertEquals("A test quote.", firstQuote.quoteText)
        assertEquals("Developer", firstQuote.authorName)
    }

    @Test
    fun apiService_getQuoteResponse_returns500ServerError() = runTest {

        val mockResponse = MockResponse()
            .setResponseCode(500)
            .setBody("<html><body>Internal Server Error</body></html>")
        mockWebServer.enqueue(mockResponse)

        try {
            apiService.getQuoteResponse(limit = 1, skip = 0)

            fail("Expected an HttpException to be thrown, but the network call succeeded.")
        } catch (e: HttpException) {
            assertEquals(500, e.code())
        }
    }
}
