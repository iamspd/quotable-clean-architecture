package com.example.quotes.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val label: String,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector
) {
    data object Feed : Screen(
        route = "feed",
        label = "Feed",
        unselectedIcon = Icons.Outlined.Home,
        selectedIcon = Icons.Default.Home
    )

    data object Collection : Screen(
        route = "collection",
        label = "Collection",
        unselectedIcon = Icons.Outlined.CheckCircle,
        selectedIcon = Icons.Default.CheckCircle
    )
}