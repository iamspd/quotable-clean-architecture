package com.example.quotes.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.quotes.ui.common.BottomBar
import com.example.quotes.ui.common.TopBar
import com.example.quotes.ui.navigation.AppNavHost
import com.example.quotes.ui.navigation.Screen

@Composable
fun QuotesApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val screens = listOf(Screen.Feed, Screen.Collection)
    val currentScreen = screens.find { it.route == currentRoute }
    val topBarTitle = currentScreen?.label ?: "Quotes App"

    Scaffold(
        topBar = { TopBar(topBarTitle) },
        bottomBar = {
        BottomBar(
            navController = navController,
            screens = screens
        )
    }) { innerPadding ->
        AppNavHost(
            navController = navController,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            startDestination = Screen.Feed.route
        )
    }
}