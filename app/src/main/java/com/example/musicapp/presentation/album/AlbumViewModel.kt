package com.example.musicapp.presentation.album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.usecases.SongUseCases
import com.example.musicapp.presentation.artist_details.ArtistDetailsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val useCases: SongUseCases
) : ViewModel() {
    private val _state = MutableStateFlow<AlbumUiState>(AlbumUiState.Loading)
    val state = _state


    fun load(albumId: String) {
        viewModelScope.launch {
            try {
                _state.value = AlbumUiState.Success(
                    albumTracks = useCases.getTracksFromAlbum(albumId)
                )

            } catch (e: Exception) {
                _state.value = AlbumUiState.Error
            }
        }
    }
}
