package com.example.musicapp.presentation.artist_details

import com.example.musicapp.domain.model.Album
import com.example.musicapp.domain.model.Artist
import com.example.musicapp.domain.model.Song

sealed interface ArtistDetailsUiState {
    data class Success(
        val artist: Artist,
        val artistTopSongs: List<Song>,
        val artistAlbums: List<Album>,
    ) : ArtistDetailsUiState
    data object Loading: ArtistDetailsUiState
    data class Error(val error: String): ArtistDetailsUiState
}