package com.example.musicapp.domain.model

sealed class SearchItem {
    data class SongItem(val song: Song, val timestamp: Long) : SearchItem()
    data class ArtistItem(val artist: Artist, val timestamp: Long) : SearchItem()
}