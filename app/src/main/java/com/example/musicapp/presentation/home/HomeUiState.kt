package com.example.musicapp.presentation.home

import com.example.musicapp.domain.model.Song

sealed interface HomeUiState {
    data class Success(
        val newTrendingSongs: List<Song>,
        val allTrendingSongs: List<Song>,
        val recommendedSongs: List<Song>
    ) : HomeUiState
    data object Loading: HomeUiState
    data object Error: HomeUiState
}