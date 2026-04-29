package com.example.musicapp.presentation.navgraph

sealed class Route(
    val route: String
) {
    object HomeScreen: Route(route = "homeScreen")
    object SearchScreen: Route(route = "searchScreen")
    object LibraryScreen: Route(route = "libraryScreen")
    object ArtistDetailsScreen: Route(route = "artistDetailsScreen")
    object AlbumScreen: Route(route = "albumScreen")
}