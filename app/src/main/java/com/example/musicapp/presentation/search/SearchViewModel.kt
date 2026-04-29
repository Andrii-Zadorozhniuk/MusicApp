package com.example.musicapp.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.model.Artist
import com.example.musicapp.domain.model.Song
import com.example.musicapp.domain.usecases.LocalUseCases
import com.example.musicapp.domain.usecases.SongUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val songUseCases: SongUseCases,
    private val localUseCases: LocalUseCases
): ViewModel() {
    private val _query = MutableStateFlow("")
    private val _filter = MutableStateFlow(SearchFilter.All)
    val query: StateFlow<String> = _query.asStateFlow()
    val filter: StateFlow<SearchFilter> = _filter.asStateFlow()

    val uiState: StateFlow<SearchUiState> = combine(_query, _filter) { query, filter ->
        query to filter
    }
        .debounce(300L)
        .filter { (query, _) -> query.length >= 2 || query.isEmpty() }
        .distinctUntilChanged()
        .flatMapLatest { (query, filter) ->
            if (query.isBlank()) {
                flowOf(SearchUiState())
            } else {
                searchFlow(query, filter)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SearchUiState()
        )

    private fun searchFlow(query: String, filter: SearchFilter): Flow<SearchUiState> = flow {
        emit(SearchUiState(isLoading = true, errorMessage = null))

        try {
            val artists: List<Artist>
            val songs: List<Song>

            coroutineScope {
                val artistsDeferred = if (filter != SearchFilter.Tracks) {
                    async { songUseCases.searchArtists(query) }
                } else null

                val songsDeferred = if (filter != SearchFilter.Artists) {
                    async { songUseCases.searchSongs(query) }
                } else null

                artists = artistsDeferred?.await() ?: emptyList()
                songs = songsDeferred?.await() ?: emptyList()
            }

            val items = buildList {
                val artistItems = artists.map { SearchUiItem.ArtistItem(it) }
                val songItems = songs.map { SearchUiItem.SongItem(it) }
                val maxSize = maxOf(artistItems.size, songItems.size)
                for (i in 0 until maxSize) {
                    if (i < songItems.size) add(songItems[i])
                    if (i < artistItems.size) add(artistItems[i])
                }
            }

            emit(SearchUiState(
                isLoading = false,
                items = items
            ))

        } catch (e: Exception) {
            emit(SearchUiState(
                isLoading = false,
                errorMessage = e.localizedMessage
            ))
        }
    }

    fun onQueryChanged(newQuery: String) {
        _query.value = newQuery
    }

    fun onFilterChanged(newFilter: SearchFilter) {
        _filter.value = newFilter
    }


    val searchHistory = localUseCases.getSearchHistory()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addSongToSearchHistory(song: Song) {
        viewModelScope.launch { localUseCases.addSongToSearchHistory(song) }
    }

    fun addArtistToSearchHistory(artist: Artist) {
        viewModelScope.launch { localUseCases.addArtistToSearchHistory(artist) }
    }

    fun clearSearchHistory() {
        viewModelScope.launch { localUseCases.clearSearchHistory() }
    }


    fun retrySearch() {
        val currentQuery = _query.value
        viewModelScope.launch {
            _query.value = currentQuery + " "
        }
    }
}