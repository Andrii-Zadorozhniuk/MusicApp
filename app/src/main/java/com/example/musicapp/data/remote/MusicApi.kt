package com.example.musicapp.data.remote

import com.example.musicapp.domain.model.response.AlbumResponse
import com.example.musicapp.domain.model.response.ArtistResponse
import com.example.musicapp.domain.model.response.MusicResponse
import com.example.musicapp.utils.Constants.API_KEY
import retrofit2.http.GET
import retrofit2.http.Url
import retrofit2.http.Query

interface MusicApi {

    @GET("tracks")
    suspend fun getSongs(
        @Query("client_id") apiKey: String = API_KEY,
        @Query("order") order: String = "popularity_total",
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0,
    ): MusicResponse

    @GET("tracks")
    suspend fun searchSongs(
        @Query("client_id") apiKey: String = API_KEY,
        @Query("namesearch") query: String,
    ): MusicResponse

    @GET("artists")
    suspend fun searchArtists(
        @Query("client_id") apiKey: String = API_KEY,
        @Query("namesearch") query: String,
    ): ArtistResponse

    @GET("tracks")
    suspend fun getTopArtistTracks(
        @Query("client_id") apiKey: String = API_KEY,
        @Query("artist_id") artistId: String,
        @Query("limit") limit: Int = 5,
        @Query("order") order: String = "popularity_total_desc",
    ): MusicResponse

    @GET("albums")
    suspend fun getArtistAlbums(
        @Query("client_id") apiKey: String = API_KEY,
        @Query("limit") limit: Int = 5,
        @Query("artist_id") artistId: String,
    ) : AlbumResponse

    @GET("tracks")
    suspend fun getTracksFromAlbum(
        @Query("client_id") apiKey: String = API_KEY,
        @Query("album_id") albumId: String,
    ) : MusicResponse


    @GET("artists")
    suspend fun getArtist(
        @Query("client_id") apiKey: String = API_KEY,
        @Query("id") artistId: String,
    ): ArtistResponse

}

//TODO
//https://api.jamendo.com/v3.0/tracks?client_id=e18ebde9&order=popularity_total&fullcount=true&offset=40