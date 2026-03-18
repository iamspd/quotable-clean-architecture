package com.example.quotes.data.ui.collection

import androidx.lifecycle.SavedStateHandle
import com.example.quotes.data.util.MainDispatcherRule
import com.example.quotes.domain.model.Quote
import com.example.quotes.domain.model.Result
import com.example.quotes.domain.usecase.AddQuoteUseCase
import com.example.quotes.domain.usecase.DeleteQuoteByIdUseCase
import com.example.quotes.domain.usecase.GetCollectionUseCase
import com.example.quotes.domain.usecase.GetQuoteByIdUseCase
import com.example.quotes.ui.collection.CollectionViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CollectionViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var getCollectionUseCase: GetCollectionUseCase
    private lateinit var addQuoteUseCase: AddQuoteUseCase
    private lateinit var getQuoteByIdUseCase: GetQuoteByIdUseCase
    private lateinit var deleteQuoteByIdUseCase: DeleteQuoteByIdUseCase

    private lateinit var viewModel: CollectionViewModel

    @Before
    fun setUp() {
        getCollectionUseCase = mockk()
        addQuoteUseCase = mockk()
        getQuoteByIdUseCase = mockk()
        deleteQuoteByIdUseCase = mockk()

        // Give the UI state a default empty flow to prevent initialization crashes
        coEvery { getCollectionUseCase() } returns emptyFlow()

        // Initialize a real SavedStateHandle, not a mock!
        // We want to test its actual internal map behavior
        savedStateHandle = SavedStateHandle()

        viewModel = CollectionViewModel(
            savedStateHandle = savedStateHandle,
            getCollectionUseCase = getCollectionUseCase,
            addQuoteUseCase = addQuoteUseCase,
            getQuoteByIdUseCase = getQuoteByIdUseCase,
            deleteQuoteByIdUseCase = deleteQuoteByIdUseCase
        )
    }

    @Test
    fun updateQuoteText_collectionViewModel_returnsSavedFlowOfText() = runTest {
        viewModel.updateQuoteText(text = "Test One")
        assertEquals("Test One", viewModel.quoteText.value)

        val savedValue = savedStateHandle.get<String>("quote_text")
        assertEquals("Test One", savedValue)
    }

    @Test
    fun addQuotes_collectionViewModel_returnsSuccessAndResetsSavedStateHandle() = runTest {
        viewModel.updateAuthorName(name = "Seneca")
        viewModel.updateQuoteText(text = "Test Quote")

        coEvery { addQuoteUseCase(quote = any()) } returns Result.Success(data = Unit)
        viewModel.addQuotes()

        val expectedQuote = Quote(id = 0, quoteText = "Test Quote", authorName = "Seneca")
        coVerify(exactly = 1) { addQuoteUseCase(expectedQuote) }

        assertEquals("", viewModel.quoteText.value)
        assertEquals("", viewModel.quoteAuthor.value)
    }
}