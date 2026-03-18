package com.example.quotes.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.quotes.ui.collection.CollectionScreen
import com.example.quotes.ui.feed.FeedScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Feed.route) {
            FeedScreen()
        }
        composable(Screen.Collection.route) {
            CollectionScreen()
        }
    }
}