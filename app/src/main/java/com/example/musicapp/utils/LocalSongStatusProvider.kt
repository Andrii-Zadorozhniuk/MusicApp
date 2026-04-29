package com.example.musicapp.utils

import androidx.compose.runtime.compositionLocalOf
import com.example.musicapp.SongStatus

val LocalSongStatusProvider = compositionLocalOf<(String) -> SongStatus> {
    { SongStatus.NOT_PLAYING }
}