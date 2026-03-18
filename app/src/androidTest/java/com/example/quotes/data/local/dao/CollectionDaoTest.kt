package com.example.quotes.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.quotes.data.local.database.AppDatabase
import com.example.quotes.data.local.entity.CollectionEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CollectionDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: CollectionDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        dao = db.collectionDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun addQuote_readFromFlow_returnsInsertedQuote() = runTest {
        val entity = CollectionEntity(id = 1, quoteText = "Test Quote.", authorName = "Developer")

        dao.addQuote(entity)
        val quoteList = dao.getAllQuotes().first()

        assertEquals(1, quoteList.size)
        assertEquals("Test Quote", quoteList[0].quoteText)
        assertEquals("Developer", quoteList[0].authorName)
    }

    @Test
    fun deleteById_removeQuoteFromDatabase_returnsSuccess() = runTest {
        val entity1 = CollectionEntity(id = 1, quoteText = "Quote 1", authorName = "Developer 1")
        val entity2 = CollectionEntity(id = 2, quoteText = "Quote 2", authorName = "Developer 2")
        dao.addQuote(entity1)
        dao.addQuote(entity2)

        dao.deleteById(id = 1)
        val quoteList = dao.getAllQuotes().first()

        assertEquals(1, quoteList.size)
        assertEquals(2, quoteList[0].id)
    }

    @Test
    fun addQuote_upsertQuote_returnsSuccess() = runTest {
        val oldEntity = CollectionEntity(id = 1, quoteText = "Old Quote", authorName = "Developer")
        dao.addQuote(oldEntity)

        val newEntity = CollectionEntity(id = 1, quoteText = "New Quote", authorName = "Developer")
        dao.addQuote(newEntity)
        val quoteList = dao.getAllQuotes().first()

        assertEquals(1, quoteList.size)
        assertEquals("New Quote", quoteList[0].quoteText)
    }
}