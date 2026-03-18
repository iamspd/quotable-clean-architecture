package com.example.quotes.ui.collection

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quotes.domain.model.Quote
import com.example.quotes.domain.model.Result
import com.example.quotes.domain.usecase.AddQuoteUseCase
import com.example.quotes.domain.usecase.DeleteQuoteByIdUseCase
import com.example.quotes.domain.usecase.GetCollectionUseCase
import com.example.quotes.domain.usecase.GetQuoteByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getCollectionUseCase: GetCollectionUseCase,
    private val addQuoteUseCase: AddQuoteUseCase,
    private val deleteQuoteByIdUseCase: DeleteQuoteByIdUseCase,
    private val getQuoteByIdUseCase: GetQuoteByIdUseCase
) : ViewModel() {

    companion object {
        private const val KEY_ID = "quote_id"
        private const val KEY_TEXT = "quote_text"
        private const val KEY_AUTHOR = "quote_author"
    }

    // TODO: Consider - currently the UiState class contains only the list
    //  is there any way to refine it?
    val uiState = getCollectionUseCase()
        .map { quote -> CollectionUiState(collection = quote) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000L),
            initialValue = CollectionUiState()
        )

    val quoteId: StateFlow<Int> = savedStateHandle.getStateFlow(key = KEY_ID, initialValue = 0)
    val quoteText: StateFlow<String> =
        savedStateHandle.getStateFlow(key = KEY_TEXT, initialValue = "")
    val quoteAuthor: StateFlow<String> =
        savedStateHandle.getStateFlow(key = KEY_AUTHOR, initialValue = "")


    fun updateQuoteText(text: String) {
        savedStateHandle[KEY_TEXT] = text
    }

    fun updateAuthorName(name: String) {
        savedStateHandle[KEY_AUTHOR] = name
    }

    fun isFormActionValid(): Boolean {
        return (quoteText.value.isNotBlank() && quoteAuthor.value.isNotBlank())
    }

    fun addQuotes() {
        if (!isFormActionValid()) return

        viewModelScope.launch {
            val quote = Quote(
                id = quoteId.value,
                quoteText = quoteText.value,
                authorName = quoteAuthor.value
            )
            when(val result = addQuoteUseCase(quote)) {
                is Result.Success -> {
                    resetForm()
                }

                is Result.Error -> {
                    triggerError(message = result.errorMessage)
                }
            }

        }
    }

    fun deleteQuote() {
        if (quoteId.value == 0) return
        viewModelScope.launch {
            deleteQuoteByIdUseCase(id = quoteId.value)
            resetForm()
        }
    }

    fun onEditQuoteClicked(id: Int) {
        viewModelScope.launch {
            val quote = getQuoteByIdUseCase(id).first()

            savedStateHandle[KEY_ID] = quote.id
            savedStateHandle[KEY_TEXT] = quote.quoteText
            savedStateHandle[KEY_AUTHOR] = quote.authorName
        }
    }

    fun resetForm() {
        savedStateHandle[KEY_ID] = 0
        savedStateHandle[KEY_TEXT] = ""
        savedStateHandle[KEY_AUTHOR] = ""
    }

    private val _uiEvent = Channel<String>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun triggerError(message: String) {
        viewModelScope.launch {
            _uiEvent.send(element = message)
        }
    }
}
