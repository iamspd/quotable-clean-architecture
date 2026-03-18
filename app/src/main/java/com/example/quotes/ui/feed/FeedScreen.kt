package com.example.quotes.ui.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.example.quotes.domain.model.Quote
import com.example.quotes.ui.common.QuoteItem

@Composable
fun FeedScreen(viewModel: FeedViewModel = hiltViewModel()) {

    val quotes = viewModel.pagingFlow.collectAsLazyPagingItems()

    Box(modifier = Modifier.fillMaxSize()) {
        FeedList(
            quotes = quotes,
            modifier = Modifier.fillMaxSize()
        )

        when (val refreshState = quotes.loadState.refresh) {
            is LoadState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is LoadState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Failed to fetch quotes:\n${refreshState.error.localizedMessage}",
                        modifier = Modifier.padding(bottom = 8.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                    Button(onClick = { quotes.retry() }) { Text("Retry") }
                }
            }

            is LoadState.NotLoading -> Unit
        }
    }
}

@Composable
fun FeedList(
    quotes: LazyPagingItems<Quote>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            count = quotes.itemCount,
            key = quotes.itemKey { it.id },
            contentType = quotes.itemContentType { "QuoteItem" }
        ) { index ->
            val quote = quotes[index]
            println("Hello there:${quote?.quoteText}")

            if (quote != null) {
                QuoteItem(
                    modifier = Modifier.fillMaxWidth(),
                    quote = quote,
                    onItemClick = {}
                )
            }
        }

        when (val appendState = quotes.loadState.append) {
            is LoadState.Loading -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            is LoadState.Error -> {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error loading more: ${appendState.error.localizedMessage}",
                        )
                        Button(onClick = { quotes.retry() }) { Text("Retry") }
                    }
                }
            }

            is LoadState.NotLoading -> Unit
        }
    }
}
