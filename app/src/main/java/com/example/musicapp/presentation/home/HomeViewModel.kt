package com.example.musicapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.musicapp.domain.model.HistoryItem
import com.example.musicapp.domain.model.Song
import com.example.musicapp.domain.usecases.LocalUseCases
import com.example.musicapp.domain.usecases.SongUseCases
import com.example.musicapp.player.PlayerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val songUseCases: SongUseCases,
    private val localUseCases: LocalUseCases
): ViewModel() {
//    private val _state = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
//    val state = _state
//    init {
//        load()
//    }
//
//    private fun load() {
//        viewModelScope.launch {
//            try {
//                _state.value = HomeUiState.Success(
//                    newTrendingSongs = songUseCases.getAllTrendingSongs(),
//                    allTrendingSongs = songUseCases.getNewTrendingSongs(),
//                    recommendedSongs = songUseCases.getRecommendedSongs()
//                )
//            } catch (e: Exception) {
//                _state.value = HomeUiState.Error
//            }
//
//        }
//    }
//
    val trendingSongs: Flow<PagingData<Song>> = songUseCases.getAllTrendingSongs()
        .cachedIn(viewModelScope)

    val newReleasesSongs: Flow<PagingData<Song>> = songUseCases.getNewTrendingSongs()
        .cachedIn(viewModelScope)

    val recommendedSongs: Flow<PagingData<Song>> = songUseCases.getRecommendedSongs()
        .cachedIn(viewModelScope)

    val recentHistory = localUseCases.getHistory()
        .map { it.take(6) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )



}