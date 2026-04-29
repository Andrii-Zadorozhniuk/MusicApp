package com.example.musicapp.domain.usecases

import com.example.musicapp.domain.usecases.local.history.AddArtistToHistory
import com.example.musicapp.domain.usecases.local.history.AddSongToHistory
import com.example.musicapp.domain.usecases.local.history.ClearHistory
import com.example.musicapp.domain.usecases.local.history.GetHistory
import com.example.musicapp.domain.usecases.local.liked.GetLikedSongs
import com.example.musicapp.domain.usecases.local.liked.IsLiked
import com.example.musicapp.domain.usecases.local.liked.LikeSong
import com.example.musicapp.domain.usecases.local.liked.UnlikeSong
import com.example.musicapp.domain.usecases.local.search.AddArtistToSearchHistory
import com.example.musicapp.domain.usecases.local.search.AddSongToSearchHistory
import com.example.musicapp.domain.usecases.local.search.ClearSearchHistory
import com.example.musicapp.domain.usecases.local.search.GetSearchHistory

class LocalUseCases(
    // likes
    val likeSong: LikeSong,
    val unlikeSong: UnlikeSong,
    val getLikedSongs: GetLikedSongs,
    val isLiked: IsLiked,

    // history
    val addSongToHistory: AddSongToHistory,
    val addArtistToHistory: AddArtistToHistory,
    val getHistory: GetHistory,
    val clearHistory: ClearHistory,

    // search
    val addSongToSearchHistory: AddSongToSearchHistory,
    val addArtistToSearchHistory: AddArtistToSearchHistory,
    val getSearchHistory: GetSearchHistory,
    val clearSearchHistory: ClearSearchHistory
)