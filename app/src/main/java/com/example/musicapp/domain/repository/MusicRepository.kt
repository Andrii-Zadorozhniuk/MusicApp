package com.example.musicapp.domain.repository

import androidx.paging.PagingData
import com.example.musicapp.domain.model.Album
import com.example.musicapp.domain.model.Artist
import com.example.musicapp.domain.model.HistoryItem
import com.example.musicapp.domain.model.SearchItem
import com.example.musicapp.domain.model.Song
import kotlinx.coroutines.flow.Flow
import com.example.musicapp.domain.exception.Result

interface MusicRepository {
    //retrofit
    fun getTrendingPaged(): Flow<PagingData<Song>>
    fun getNewReleasesPaged(): Flow<PagingData<Song>>
    fun getRecommendedPaged(): Flow<PagingData<Song>>
    suspend fun searchSongs(query: String): Result<List<Song>>
    suspend fun searchArtists(query: String): Result<List<Artist>>
    suspend fun getTopArtistTracks(artistId: String): Result<List<Song>>
    suspend fun getArtistAlbums(artistId: String): Result<List<Album>>
    suspend fun getTracksFromAlbum(albumId: String): Result<List<Song>>
    suspend fun getArtist(artistId: String): Result<Artist>

    //likes
    suspend fun likeSong(song: Song)
    suspend fun unlikeSong(id: String)
    fun getLikedSongs(): Flow<List<Song>>
    fun isLiked(id: String): Flow<Boolean>

    //history
    suspend fun addSongToHistory(song: Song)
    suspend fun addArtistToHistory(artist: Artist)
    fun getHistory(): Flow<List<HistoryItem>>
    suspend fun clearHistory()

    //search
    suspend fun addSongToSearchHistory(song: Song)
    suspend fun addArtistToSearchHistory(artist: Artist)
    fun getSearchHistory(): Flow<List<SearchItem>>
    suspend fun clearSearchHistory()

}

