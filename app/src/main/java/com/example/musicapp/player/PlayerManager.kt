package com.example.musicapp.player

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import com.example.musicapp.domain.model.Song
import com.example.musicapp.utils.Constants.DEFAULT_MP3
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlayerManager(
    private val controllerFuture: ListenableFuture<MediaController>
) {
    private var controller: MediaController? = null

    private val _state = MutableStateFlow(PlayerState())
    val state: StateFlow<PlayerState> = _state

    private var isControllerReady = false
    private var pendingSong: Song? = null

    init {
        controllerFuture.addListener({
            controller = controllerFuture.get()
            isControllerReady = true
            setupPlayer()
            pendingSong?.let { playPlaylist(listOf(it), 0, false) }
            pendingSong = null
        }, Dispatchers.Main.asExecutor())
    }

    private fun setupPlayer() {
        val currentController = controller ?: return

        currentController.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _state.update { it.copy(isPlaying = isPlaying) }
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                _state.update { it.copy(currentSongIndex = currentController.currentMediaItemIndex) }
            }
        })

        CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                controller?.let {
                    val position = it.currentPosition
                    val duration = it.duration.takeIf { it > 0 } ?: 1
                    _state.update { s ->
                        s.copy(progress = position.toFloat() / duration, position = position, duration = duration)
                    }
                }
                delay(500)
            }
        }
    }

    fun playPlaylist(songs: List<Song>, startIndex: Int, isPlaying: Boolean = true) {
        val currentController = controller ?: return

        _state.value = PlayerState(playlist = songs, currentSongIndex = startIndex, isPlaying = isPlaying)

        val mediaItems = songs.map {
            MediaItem.Builder()
                .setMediaId(it.id.toString())
                .setUri(it.audio)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(it.name)
                        .setArtist(it.artistName)
                        .build()
                )
                .build()
        }
        currentController.setMediaItems(mediaItems, startIndex, 0L)
        currentController.prepare()
        if (isPlaying == true) currentController.play()
    }

    fun togglePlayPause() {
        controller?.let {
            if (it.isPlaying) it.pause() else it.play()
        }
    }

    fun next() {
        controller?.seekToNext()
    }

    fun previous() {
        controller?.seekToPrevious()
    }

    fun seekTo(position: Long) {
        controller?.seekTo(position)
    }

    fun updateCurrentSong(song: Song) {
        if (isControllerReady) {
            playPlaylist(listOf(song), 0, false)
        } else {
            pendingSong = song
        }
    }

}