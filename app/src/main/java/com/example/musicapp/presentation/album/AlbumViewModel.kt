package com.example.musicapp.presentation.album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.exception.AppException
import com.example.musicapp.domain.exception.Result
import com.example.musicapp.domain.usecases.SongUseCases
import com.example.musicapp.presentation.artist_details.ArtistDetailsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
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
            _state.value = AlbumUiState.Loading

            coroutineScope {
                val songsDeferred = async{useCases.getTracksFromAlbum(albumId)}
                val songsResult = songsDeferred.await()

                val error = (songsResult as? Result.Error)?.exception
                if (error != null) {
                    _state.value = AlbumUiState.Error(
                        when(error) {
                            is AppException.NoInternet -> "No internet connection"
                            is AppException.ServerError -> "Server error"
                            else -> "Something went wrong"
                        }
                    )
                    return@coroutineScope
                }
                val songs = (songsResult as Result.Success).data
                _state.value = AlbumUiState.Success(songs)
            }
        }
    }
}
