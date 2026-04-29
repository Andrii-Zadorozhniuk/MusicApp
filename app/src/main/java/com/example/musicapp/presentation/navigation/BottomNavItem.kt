package com.example.musicapp.presentation.navigation

import android.graphics.drawable.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.musicapp.presentation.navgraph.Route

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    object Home : BottomNavItem(Route.HomeScreen.route, Icons.Default.Home, "Home")
    object Search : BottomNavItem(Route.SearchScreen.route, Icons.Default.Search, "Search")
    object Library : BottomNavItem(Route.LibraryScreen.route, Icons.Outlined.VideoLibrary, "Library")
}