package com.example.musicapp.domain.usecases

import com.example.musicapp.domain.usecases.last_song.ReadLastSong
import com.example.musicapp.domain.usecases.last_song.SaveLastSong

class LastSongUseCases(
    val saveLastSong: SaveLastSong,
    val readLastSong: ReadLastSong
)