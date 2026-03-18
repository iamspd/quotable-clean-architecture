package com.example.quotes.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        modifier = Modifier.fillMaxWidth()
    )
}
