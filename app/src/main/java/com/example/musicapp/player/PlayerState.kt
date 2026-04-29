package com.example.musicapp.player

import com.example.musicapp.domain.model.Song

data class PlayerState(
    val playlist: List<Song> = emptyList(),
    val currentSongIndex: Int = 0,
    val isPlaying: Boolean = false,
    val duration: Long = 0L,
    val position: Long = 0L,
    val progress: Float = 0f
) {
    val currentSong: Song?
        get() = playlist.getOrNull(currentSongIndex)
}
