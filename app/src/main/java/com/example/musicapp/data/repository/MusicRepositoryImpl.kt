package com.example.musicapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.musicapp.data.local.AppDatabase
import com.example.musicapp.data.local.dao.ArtistDao
import com.example.musicapp.data.local.dao.SongDao
import com.example.musicapp.data.local.entity.ArtistCategory
import com.example.musicapp.data.local.entity.SongCategory
import com.example.musicapp.data.local.mapper.ArtistMapper.toDomain
import com.example.musicapp.data.local.mapper.ArtistMapper.toEntity
import com.example.musicapp.data.local.mapper.SongMapper.toDomain
import com.example.musicapp.data.local.mapper.SongMapper.toEntity
import com.example.musicapp.data.paging.SongRemoteMediator
import com.example.musicapp.data.remote.MusicApi
import com.example.musicapp.data.remote.toAppException
import com.example.musicapp.domain.model.Album
import com.example.musicapp.domain.model.Artist
import com.example.musicapp.domain.model.HistoryItem
import com.example.musicapp.domain.model.SearchItem
import com.example.musicapp.domain.model.Song
import com.example.musicapp.domain.repository.MusicRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import com.example.musicapp.domain.exception.Result
import com.example.musicapp.utils.safeApiCall

class MusicRepositoryImpl(
    private val api: MusicApi,
    private val songDao: SongDao,
    private val artistDao: ArtistDao,
    private val db: AppDatabase
): MusicRepository {
    @OptIn(ExperimentalPagingApi::class)
    override fun getTrendingPaged(): Flow<PagingData<Song>> = Pager(
        config = PagingConfig(pageSize = 10, enablePlaceholders = false),
        remoteMediator = SongRemoteMediator(api, db, SongCategory.TRENDING, "popularity_total"),
        pagingSourceFactory = { songDao.getSongsPaged(SongCategory.TRENDING) }
    ).flow.map { it.map { entity -> entity.toDomain() } }

    @OptIn(ExperimentalPagingApi::class)
    override fun getNewReleasesPaged(): Flow<PagingData<Song>> = Pager(
        config = PagingConfig(pageSize = 10, enablePlaceholders = false),
        remoteMediator = SongRemoteMediator(api, db, SongCategory.NEW_RELEASES, "releasedate_desc"),
        pagingSourceFactory = { songDao.getSongsPaged(SongCategory.NEW_RELEASES) }
    ).flow.map { it.map { entity -> entity.toDomain() } }

    @OptIn(ExperimentalPagingApi::class)
    override fun getRecommendedPaged(): Flow<PagingData<Song>> = Pager(
        config = PagingConfig(pageSize = 10, enablePlaceholders = false),
        remoteMediator = SongRemoteMediator(api, db, SongCategory.RECOMMENDED, "popularity_week"),
        pagingSourceFactory = { songDao.getSongsPaged(SongCategory.RECOMMENDED) }
    ).flow.map { it.map { entity -> entity.toDomain() } }



    override suspend fun searchSongs(query: String): Result<List<Song>> =
        safeApiCall { api.searchSongs(query = query).results }

    override suspend fun searchArtists(query: String): Result<List<Artist>> =
        safeApiCall { api.searchArtists(query = query).results }


    override suspend fun getTopArtistTracks(artistId: String): Result<List<Song>> =
        safeApiCall { api.getTopArtistTracks(artistId = artistId).results }

    override suspend fun getArtistAlbums(artistId: String): Result<List<Album>> =
        safeApiCall { api.getArtistAlbums(artistId = artistId).results }


    override suspend fun getTracksFromAlbum(albumId: String): Result<List<Song>> =
        safeApiCall { api.getTracksFromAlbum(albumId = albumId).results }


    override suspend fun getArtist(artistId: String): Result<Artist> =
        safeApiCall { api.getArtist(artistId = artistId).results.first() }


    override suspend fun likeSong(song: Song) {
        songDao.insert(song.toEntity(SongCategory.LIKED))
    }

    override suspend fun unlikeSong(id: String) {
        songDao.delete(id, SongCategory.LIKED)
    }

    override fun getLikedSongs(): Flow<List<Song>> {
        return songDao.getSongsByCategory(SongCategory.LIKED)
            .map { list -> list.map { it.toDomain() } }
    }

    override fun isLiked(id: String): Flow<Boolean> {
        return songDao.isLiked(id)
    }

    override suspend fun addSongToHistory(song: Song) {
        songDao.insertWithLimit(song.toEntity(SongCategory.HISTORY))
    }

    override suspend fun addArtistToHistory(artist: Artist) {
        artistDao.insertWithLimit(artist.toEntity(ArtistCategory.HISTORY))
    }

    override fun getHistory(): Flow<List<HistoryItem>> {
        return combine(
            songDao.getSongsByCategory(SongCategory.HISTORY),
            artistDao.getArtistsByCategory(ArtistCategory.HISTORY)
        ) { songs, artists ->
            val songItems = songs.map {
                HistoryItem.SongItem(it.toDomain(), it.timestamp)
            }
            val artistItems = artists.map {
                HistoryItem.ArtistItem(it.toDomain(), it.timestamp)
            }
            (songItems + artistItems).sortedByDescending {
                when (it) {
                    is HistoryItem.SongItem -> it.timestamp
                    is HistoryItem.ArtistItem -> it.timestamp
                }
            }
        }
    }

    override suspend fun clearHistory() {
        songDao.deleteByCategory(SongCategory.HISTORY)
        artistDao.deleteByCategory(ArtistCategory.HISTORY)
    }

    override suspend fun addSongToSearchHistory(song: Song) {
        songDao.insertWithLimit(song.toEntity(SongCategory.SEARCH))
    }

    override suspend fun addArtistToSearchHistory(artist: Artist) {
        artistDao.insertWithLimit(artist.toEntity(ArtistCategory.SEARCH))
    }

    override fun getSearchHistory(): Flow<List<SearchItem>> {
        return combine(
            songDao.getSongsByCategory(SongCategory.SEARCH),
            artistDao.getArtistsByCategory(ArtistCategory.SEARCH)
        ) { songs, artists ->
            val songItems = songs.map {
                SearchItem.SongItem(it.toDomain(), it.timestamp)
            }
            val artistItems = artists.map {
                SearchItem.ArtistItem(it.toDomain(), it.timestamp)
            }
            (songItems + artistItems).sortedByDescending {
                when (it) {
                    is SearchItem.SongItem -> it.timestamp
                    is SearchItem.ArtistItem -> it.timestamp
                }
            }
        }
    }

    override suspend fun clearSearchHistory() {
        songDao.deleteByCategory(SongCategory.SEARCH)
        artistDao.deleteByCategory(ArtistCategory.SEARCH)
    }

}