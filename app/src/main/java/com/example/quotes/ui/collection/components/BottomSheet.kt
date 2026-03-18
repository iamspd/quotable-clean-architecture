package com.example.quotes.ui.collection.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
    quoteText: String,
    onQuoteChanged: (String) -> Unit,
    authorName: String,
    onAuthorChanged: (String) -> Unit,
    isQuoteExists: Boolean,
    onDeleteClicked: () -> Unit,
    onCancelClicked: () -> Unit,
    onSaveClicked: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = if (isQuoteExists) "Edit Quote" else "Add Quote",
                style = MaterialTheme.typography.headlineMedium
            )
            HorizontalDivider()
            TextField(
                value = quoteText,
                onValueChange = onQuoteChanged,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Quote") }
            )
            TextField(
                value = authorName,
                onValueChange = onAuthorChanged,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Author") }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (isQuoteExists) {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = onDeleteClicked,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        )
                    ) { Text("Delete") }
                } else {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = onCancelClicked
                    ) { Text("Cancel") }
                }
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = onSaveClicked
                ) { Text("Save") }
            }
        }
    }
}