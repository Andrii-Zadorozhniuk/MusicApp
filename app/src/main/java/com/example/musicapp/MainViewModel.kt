package com.example.musicapp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.model.Artist
import com.example.musicapp.domain.model.Song
import com.example.musicapp.domain.usecases.LastSongUseCases
import com.example.musicapp.domain.usecases.LocalUseCases
import com.example.musicapp.domain.usecases.SongUseCases
import com.example.musicapp.player.PlayerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    val playerManager: PlayerManager,
    private val localUseCases: LocalUseCases,
    private val lastSongUseCases: LastSongUseCases
) : ViewModel() {

    init {
        viewModelScope.launch {
            lastSongUseCases.readLastSong().first()?.let { song ->
                playerManager.updateCurrentSong(song)
            }
        }
    }
    fun onSongClick(list: List<Song>, index: Int) {
        playerManager.playPlaylist(list, index)
        viewModelScope.launch {
            localUseCases.addSongToHistory(list[index])
            lastSongUseCases.saveLastSong(list[index])
        }
    }
    fun songStatus(songId: String) : SongStatus {
        val currentState = playerManager.state.value
        val currentSong = currentState.currentSong
        return when {
            currentSong == null || currentSong.id.toString() != songId -> SongStatus.NOT_PLAYING
            currentState.isPlaying -> SongStatus.IS_PLAYING
            else -> SongStatus.PAUSED
        }
    }

    fun getPlaylist(): List<Song> {
        return playerManager.state.value.playlist
    }

    fun likeSong(song: Song) {
        viewModelScope.launch { localUseCases.likeSong(song) }
    }

    fun unlikeSong(id: String) {
        viewModelScope.launch { localUseCases.unlikeSong(id) }
    }

    fun isLiked(id: String): Flow<Boolean> = localUseCases.isLiked(id)
}

enum class SongStatus {
    IS_PLAYING, PAUSED, NOT_PLAYING
}