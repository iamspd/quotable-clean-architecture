package com.example.quotes.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.quotes.data.mappers.toQuote
import com.example.quotes.data.remote.api.ApiService
import com.example.quotes.domain.model.Quote
import retrofit2.HttpException
import java.io.IOException

class QuotePagingSource(
    private val apiService: ApiService,
    private val limit: Int = 30
) : PagingSource<Int, Quote>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Quote> {
        val currentSkip = params.key ?: 0

        return try {
            val response = apiService.getQuoteResponse(limit = limit, skip = currentSkip)
            val quotes = response.quotes.map { it.toQuote() }

            val nextKey =
                if (quotes.isEmpty() || quotes.size < limit) null
                else currentSkip + limit

            LoadResult.Page(
                data = quotes,
                prevKey = if (currentSkip == 0) null else currentSkip - limit,
                nextKey = nextKey
            )
        } catch (e: IOException) {
            LoadResult.Error(throwable = e)
        } catch (e: HttpException) {
            LoadResult.Error(throwable = e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Quote>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(limit) ?: anchorPage?.nextKey?.minus(limit)
        }
    }
}
