package com.example.musicapp.presentation.search

import com.example.musicapp.domain.model.Artist
import com.example.musicapp.domain.model.Song

sealed interface SearchUiItem {
    data class ArtistItem(val artist: Artist) : SearchUiItem
    data class SongItem(val track: Song) : SearchUiItem
}


data class SearchUiState(
    val isLoading: Boolean = false,
    val items: List<SearchUiItem> = emptyList(),
    val errorMessage: String? = null,
)

enum class SearchFilter {
    All, Artists, Tracks
}