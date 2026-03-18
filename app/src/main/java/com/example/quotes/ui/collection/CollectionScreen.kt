package com.example.quotes.ui.collection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.quotes.ui.collection.components.BottomSheet
import com.example.quotes.ui.common.QuoteItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionScreen(viewModel: CollectionViewModel = hiltViewModel()) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val quoteId by viewModel.quoteId.collectAsStateWithLifecycle()
    val quoteText by viewModel.quoteText.collectAsStateWithLifecycle()
    val authorName by viewModel.quoteAuthor.collectAsStateWithLifecycle()

    var showBottomSheet by rememberSaveable { mutableStateOf(value = false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val snackBarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(key1 = viewModel.uiEvent, key2 = lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.uiEvent.collect { message ->
                snackBarHostState.showSnackbar(message)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 12.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items = uiState.collection, key = { it.id }) { quote ->
                QuoteItem(
                    quote = quote,
                    modifier = Modifier.fillMaxWidth(),
                    onItemClick = {
                        showBottomSheet = true
                        viewModel.onEditQuoteClicked(id = it)
                    }
                )
            }
        }
        FloatingActionButton(
            onClick = {
                viewModel.resetForm()
                showBottomSheet = true
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(all = 16.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Quote")
        }
        SnackbarHost(
            hostState = snackBarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp)
        )
        if (showBottomSheet) {
            BottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
                quoteText = quoteText,
                onQuoteChanged = { viewModel.updateQuoteText(text = it) },
                authorName = authorName,
                onAuthorChanged = { viewModel.updateAuthorName(name = it) },
                isQuoteExists = quoteId != 0,
                onDeleteClicked = {
                    viewModel.deleteQuote()
                    showBottomSheet = false
                },
                onCancelClicked = { showBottomSheet = false },
                onSaveClicked = {
                    viewModel.addQuotes()
                    showBottomSheet = false
                }
            )
        }
    }
}