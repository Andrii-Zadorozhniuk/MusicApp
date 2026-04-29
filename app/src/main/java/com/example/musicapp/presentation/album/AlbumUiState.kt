package com.example.musicapp.presentation.album

import com.example.musicapp.domain.model.Album
import com.example.musicapp.domain.model.Song

sealed interface AlbumUiState {
    data class Success(
        val albumTracks: List<Song>,
    ) : AlbumUiState
    data object Loading: AlbumUiState
    data object Error: AlbumUiState
}