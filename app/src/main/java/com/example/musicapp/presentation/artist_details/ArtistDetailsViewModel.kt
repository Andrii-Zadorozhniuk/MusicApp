package com.example.musicapp.presentation.artist_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.exception.AppException
import com.example.musicapp.domain.model.Artist
import com.example.musicapp.domain.usecases.LocalUseCases
import com.example.musicapp.domain.usecases.SongUseCases

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.musicapp.domain.exception.Result

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
//    fun load(artistId: String) {
//        viewModelScope.launch {
//            try {
//                val artist = async{ useCases.getArtist(artistId)}
//                val songs = async {useCases.getTopArtistTracks(artistId)}
//                val albums = async{ useCases.getArtistAlbum(artistId)}
//                _state.value = ArtistDetailsUiState.Success(
//                    artistTopSongs = songs.await(),
//                    artistAlbums = albums.await(),
//                    artist = artist.await()
//                )
//                addArtistToHistory(artist.await())
//
//            } catch (e: Exception) {
//                _state.value = ArtistDetailsUiState.Error(e.message ?: "Unknown error")
//            }
//        }
//    }

    fun load(artistId: String) {
        viewModelScope.launch {
            _state.value = ArtistDetailsUiState.Loading
            coroutineScope {
                val artistDeferred = async { useCases.getArtist(artistId) }
                val songsDeferred = async { useCases.getTopArtistTracks(artistId) }
                val albumsDeferred = async { useCases.getArtistAlbum(artistId) }

                val artistResult = artistDeferred.await()
                val songsResult = songsDeferred.await()
                val albumsResult = albumsDeferred.await()

                val error = (artistResult as? Result.Error)?.exception
                    ?: (songsResult as? Result.Error)?.exception
                    ?: (albumsResult as? Result.Error)?.exception

                if (error != null) {
                    _state.value = ArtistDetailsUiState.Error(
                        when (error) {
                            is AppException.NoInternet -> "No internet connection"
                            is AppException.ServerError -> "Server error"
                            else -> "Something went wrong"
                        }
                    )
                    return@coroutineScope
                }
                val artist = (artistResult as Result.Success).data
                val songs = (songsResult as Result.Success).data
                val albums = (albumsResult as Result.Success).data

                _state.value = ArtistDetailsUiState.Success(
                    artist = artist,
                    artistTopSongs = songs,
                    artistAlbums = albums
                )
                addArtistToHistory(artist)
            }

        }
    }
}