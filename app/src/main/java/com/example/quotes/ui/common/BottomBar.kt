package com.example.quotes.ui.common

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.quotes.ui.navigation.Screen

@Composable
fun BottomBar(
    navController: NavHostController,
    screens: List<Screen>
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(modifier = Modifier.fillMaxWidth()) {
        screens.forEach { screen ->
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(route = screen.route) {
                        popUpTo(Screen.Feed.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Crossfade(
                        targetState = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        label = "NavBarAnimFade"
                    ) { isSelected ->
                        val image = if (isSelected) screen.selectedIcon else screen.unselectedIcon
                        Icon(
                            imageVector = image,
                            contentDescription = screen.label
                        )
                    }
                },
                label = { Text(screen.label) }
            )
        }
    }
}
