package com.example.musicapp.domain.usecases.network

import androidx.paging.PagingData
import com.example.musicapp.domain.model.Song
import com.example.musicapp.domain.repository.MusicRepository
import kotlinx.coroutines.flow.Flow

class GetAllTrendingSongs(
    private val repository: MusicRepository
) {
    operator fun invoke(): Flow<PagingData<Song>> = repository.getTrendingPaged()
}