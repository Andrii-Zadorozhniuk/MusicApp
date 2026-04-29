package com.example.musicapp.presentation.artist_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.model.Artist
import com.example.musicapp.domain.usecases.LocalUseCases
import com.example.musicapp.domain.usecases.SongUseCases
import com.example.musicapp.presentation.home.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistDetailsViewModel @Inject constructor(
    private val useCases: SongUseCases,
    private val localUseCases: LocalUseCases
) : ViewModel() {

    private val _state = MutableStateFlow<ArtistDetailsUiState>(ArtistDetailsUiState.Loading)
    val state = _state


    fun addArtistToHistory(artist: Artist) {
        viewModelScope.launch {
            localUseCases.addArtistToHistory(artist)
        }
    }
    fun load(artistId: String) {
        viewModelScope.launch {
            try {
                _state.value = ArtistDetailsUiState.Success(
                    artistTopSongs = useCases.getTopArtistTracks(artistId),
                    artistAlbums = useCases.getArtistAlbum(artistId)
                )

            } catch (e: Exception) {
                _state.value = ArtistDetailsUiState.Error
            }
        }
    }
}