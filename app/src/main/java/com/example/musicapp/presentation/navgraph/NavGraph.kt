package com.example.musicapp.presentation.navgraph

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.musicapp.SongStatus
import com.example.musicapp.domain.model.Album
import com.example.musicapp.domain.model.Artist
import com.example.musicapp.domain.model.Song
import com.example.musicapp.presentation.album.AlbumScreen
import com.example.musicapp.presentation.album.AlbumViewModel
import com.example.musicapp.presentation.artist_details.ArtistDetailsScreen
import com.example.musicapp.presentation.artist_details.ArtistDetailsViewModel
import com.example.musicapp.presentation.home.HomeScreen
import com.example.musicapp.presentation.home.HomeViewModel
import com.example.musicapp.presentation.library.LibraryScreen
import com.example.musicapp.presentation.library.LibraryViewModel
import com.example.musicapp.presentation.search.SearchScreen
import com.example.musicapp.presentation.search.SearchViewModel
import kotlinx.coroutines.flow.Flow


@Composable
fun NavGraph(
    navController: NavHostController,
    onSongClick: (List<Song>, index: Int) -> Unit,
    onLike: (Song) -> Unit,
    onUnlike: (String) -> Unit,
    isLiked: (String) -> Flow<Boolean>
) {

    NavHost(navController, startDestination = Route.HomeScreen.route) {
        composable(route = Route.HomeScreen.route) {
            val viewModel: HomeViewModel = hiltViewModel()
            //val state = viewModel.state.collectAsState().value
            val allTrendingSongs = viewModel.trendingSongs.collectAsLazyPagingItems()
            val newTrendingSongs = viewModel.newReleasesSongs.collectAsLazyPagingItems()
            val recommendedSongs = viewModel.recommendedSongs.collectAsLazyPagingItems()
            val recentHistory by viewModel.recentHistory.collectAsState()
            HomeScreen(
                allTrendingSongs = allTrendingSongs,
                newTrendingSongs = newTrendingSongs,
                recommendedSongs = recommendedSongs,
                onSongClick = {list, index -> onSongClick(list, index)},
                onArtistClick = {navigateToArtistDetails(navController, it)},
                recentHistory = recentHistory
            )
        }
        composable(route = Route.SearchScreen.route) {
            val viewModel: SearchViewModel = hiltViewModel()
            val state by viewModel.uiState.collectAsState()
            val query by viewModel.query.collectAsState()
            val filter by viewModel.filter.collectAsState()
            val history by viewModel.searchHistory.collectAsState()
            SearchScreen(
                clearSearchHistory = viewModel::clearSearchHistory,
                history = history,
                state = state,
                query = query,
                filter = filter,
                onQueryChange = viewModel::onQueryChanged,
                onSongClick = { list, index ->
                    viewModel.addSongToSearchHistory(list[index])
                    onSongClick(list, index)
                              },
                onArtistClick = {
                    viewModel.addArtistToSearchHistory(it)
                    navigateToArtistDetails(navController, it)
                                },
                onChangeFilter = viewModel::onFilterChanged,
                onLike = onLike,
                onUnlike = onUnlike,
                isLiked = isLiked,
                onRetry = viewModel::retrySearch
            )
        }

        composable(route = Route.LibraryScreen.route) {
            val viewModel: LibraryViewModel = hiltViewModel()
            val likedSongs by viewModel.likedSongs.collectAsState()
            val libraryHistory by viewModel.libraryHistory.collectAsState()
            val isLoading by viewModel.isLoading.collectAsState()
            LibraryScreen(
                likedSongs = likedSongs,
                libraryHistory = libraryHistory,
                onSongClick = {list, index -> onSongClick(list, index)},
                onArtistClick = {navigateToArtistDetails(navController, it)},
                onLike = onLike,
                onUnlike = onUnlike,
                isLiked = isLiked,
                isLoading = isLoading,
                goToSearch = {navController.navigate(route = Route.SearchScreen.route)}
            )
        }
        composable(route = Route.ArtistDetailsScreen.route) {
            val viewModel: ArtistDetailsViewModel = hiltViewModel()
            val state = viewModel.state.collectAsState().value
            navController.previousBackStackEntry?.savedStateHandle?.get<Artist>("artist")?.let { artist ->
                viewModel.addArtistToHistory(artist)
                ArtistDetailsScreen(
                    onLoad = {viewModel.load(artist.id.toString())},
                    state = state,
                    artist = artist,
                    onBack = {navController.popBackStack()},
                    onSongClick = {list, index -> onSongClick(list, index)},
                    onAlbumClick = {navigateToAlbum(navController,it)}
                )
            }
        }
        composable(route = Route.AlbumScreen.route) {
            val viewModel: AlbumViewModel = hiltViewModel()
            val state = viewModel.state.collectAsState().value
            navController.previousBackStackEntry?.savedStateHandle?.get<Album>("album")?.let { album ->
                AlbumScreen(
                    album = album,
                    onLoad = {viewModel.load(album.id.toString())},
                    state = state,
                    onBack = {navController.popBackStack()},
                    onSongClick = {list, index -> onSongClick(list, index)},
                    onLike = onLike,
                    onUnlike = onUnlike,
                    isLiked = isLiked
                )
            }
        }
    }
}


private fun navigateToArtistDetails(navController: NavController, artist: Artist) {
    navController.currentBackStackEntry?.savedStateHandle?.set("artist", artist)
    navController.navigate(route = Route.ArtistDetailsScreen.route)
}
private fun navigateToAlbum(navController: NavController, album: Album) {
    navController.currentBackStackEntry?.savedStateHandle?.set("album", album)
    navController.navigate(route = Route.AlbumScreen.route)
}