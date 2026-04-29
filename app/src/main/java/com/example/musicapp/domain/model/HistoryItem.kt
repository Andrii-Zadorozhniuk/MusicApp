package com.example.musicapp.domain.model

sealed class HistoryItem {
    data class SongItem(val song: Song, val timestamp: Long) : HistoryItem()
    data class ArtistItem(val artist: Artist, val timestamp: Long) : HistoryItem()
}