package com.example.musicapp.domain.usecases

import com.example.musicapp.domain.usecases.network.GetAllTrendingSongs
import com.example.musicapp.domain.usecases.network.GetArtist
import com.example.musicapp.domain.usecases.network.GetArtistAlbums
import com.example.musicapp.domain.usecases.network.GetNewTrendingSongs
import com.example.musicapp.domain.usecases.network.GetRecommendedSongs
import com.example.musicapp.domain.usecases.network.GetTopArtistTracks
import com.example.musicapp.domain.usecases.network.GetTracksFromAlbum
import com.example.musicapp.domain.usecases.network.SearchArtists
import com.example.musicapp.domain.usecases.network.SearchSongs

class SongUseCases(
    val getAllTrendingSongs: GetAllTrendingSongs,
    val getNewTrendingSongs: GetNewTrendingSongs,
    val getRecommendedSongs: GetRecommendedSongs,
    val searchSongs: SearchSongs,
    val searchArtists: SearchArtists,

    val getArtistAlbum: GetArtistAlbums,
    val getTopArtistTracks: GetTopArtistTracks,
    val getTracksFromAlbum: GetTracksFromAlbum,

    val getArtist: GetArtist
)


