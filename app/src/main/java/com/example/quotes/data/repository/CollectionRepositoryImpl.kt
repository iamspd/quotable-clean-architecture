package com.example.quotes.data.repository

import com.example.quotes.data.local.dao.CollectionDao
import com.example.quotes.data.mappers.toCollectionEntity
import com.example.quotes.data.mappers.toQuote
import com.example.quotes.domain.model.Quote
import com.example.quotes.domain.model.Result
import com.example.quotes.domain.repository.CollectionRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CollectionRepositoryImpl @Inject constructor(
    private val collectionDao: CollectionDao
) : CollectionRepository {
    override suspend fun addQuote(quote: Quote): Result<Unit> {
        return try {
            collectionDao.addQuote(quote.toCollectionEntity())
            Result.Success(data = Unit)
        } catch (e: Exception) {
            /**
             * This is for when coroutine is cancelled by the user action
             *  by pressing back or clearing the app from recent tray.
             * To make sure that the app does not have impending tasks while in background  -
             *  consuming resources for nothing.
              */
            if (e is CancellationException) throw e
            Result.Error(
                errorMessage = "Failed to save quote.",
                exception = e
            )
        }

    }

    override suspend fun deleteQuote(id: Int): Result<Unit> {
        return try {
            collectionDao.deleteById(id)
            Result.Success(data = Unit)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.Error(
                errorMessage = "Failed to delete quote.",
                exception = e
            )
        }
    }

    override fun getAllQuotes(): Flow<List<Quote>> {
        return collectionDao.getAllQuotes().map { collectionEntities ->
            collectionEntities.map { it.toQuote() }
        }
    }

    override fun getQuoteById(id: Int): Flow<Quote> {
        return collectionDao.getQuoteById(id).map { it.toQuote() }
    }
}
