package com.example.musicapp.di

import android.app.Application
import android.content.ComponentName
import android.content.Context
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.PlaybackParameters
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.musicapp.data.local.AppDatabase
import com.example.musicapp.data.local.dao.ArtistDao
import com.example.musicapp.data.local.dao.SongDao
import com.example.musicapp.data.manager.PlayerPreferencesManagerImpl
import com.example.musicapp.data.remote.MusicApi
import com.example.musicapp.data.repository.MusicRepositoryImpl
import com.example.musicapp.domain.manager.PlayerPreferencesManager
import com.example.musicapp.domain.repository.MusicRepository
import com.example.musicapp.domain.usecases.LastSongUseCases
import com.example.musicapp.domain.usecases.network.GetAllTrendingSongs
import com.example.musicapp.domain.usecases.network.GetArtist
import com.example.musicapp.domain.usecases.network.GetNewTrendingSongs
import com.example.musicapp.domain.usecases.network.GetRecommendedSongs
import com.example.musicapp.domain.usecases.network.SearchArtists
import com.example.musicapp.domain.usecases.network.SearchSongs
import com.example.musicapp.domain.usecases.SongUseCases
import com.example.musicapp.domain.usecases.network.GetArtistAlbums
import com.example.musicapp.domain.usecases.network.GetTopArtistTracks
import com.example.musicapp.domain.usecases.network.GetTracksFromAlbum
import com.example.musicapp.player.MusicService
import com.example.musicapp.player.PlayerManager
import com.example.musicapp.utils.Constants.BASE_URL
import com.google.common.util.concurrent.ListenableFuture
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.musicapp.domain.usecases.LocalUseCases
import com.example.musicapp.domain.usecases.last_song.ReadLastSong
import com.example.musicapp.domain.usecases.last_song.SaveLastSong
import com.example.musicapp.domain.usecases.local.liked.*
import com.example.musicapp.domain.usecases.local.history.*
import com.example.musicapp.domain.usecases.local.search.*

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMusicApi(): MusicApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MusicApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMusicRepository(
        api: MusicApi,
        songDao: SongDao,
        artistDao: ArtistDao,
        db: AppDatabase
    ): MusicRepository {
        return MusicRepositoryImpl(
            api = api,
            songDao = songDao,
            artistDao = artistDao,
            db = db
        )
    }

    @Provides
    @Singleton
    fun provideExoPlayer(@ApplicationContext context: Context): ExoPlayer {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .build()

        return ExoPlayer.Builder(context)
            .setAudioAttributes(audioAttributes, true)
            .setHandleAudioBecomingNoisy(true)
            .build().apply {
                setPlaybackParameters(PlaybackParameters(1f))
            }
    }

    @Provides
    @Singleton
    fun provideSessionToken(app: Application): SessionToken {
        return SessionToken(app, ComponentName(app, MusicService::class.java))
    }

//    @Provides
//    @Singleton
//    fun provideMediaController(app: Application, token: SessionToken): MediaController {
//        return MediaController.Builder(app, token).buildAsync().get()
//    }

    @Provides
    @Singleton
    fun provideMediaControllerFuture(app: Application, token: SessionToken): ListenableFuture<MediaController> {
        return MediaController.Builder(app, token).buildAsync()
    }

    @Provides
    @Singleton
    fun providePlayerManager(controllerFuture: ListenableFuture<MediaController>): PlayerManager {
        return PlayerManager(controllerFuture)
    }


    @Provides
    @Singleton
    fun provideSongUseCases(repository: MusicRepository): SongUseCases {
        return SongUseCases(
            getAllTrendingSongs = GetAllTrendingSongs(repository),
            getNewTrendingSongs = GetNewTrendingSongs(repository),
            getRecommendedSongs = GetRecommendedSongs(repository),
            searchSongs = SearchSongs(repository),
            searchArtists = SearchArtists(repository),
            getArtistAlbum = GetArtistAlbums(repository),
            getTopArtistTracks = GetTopArtistTracks(repository),
            getTracksFromAlbum = GetTracksFromAlbum(repository),
            getArtist = GetArtist(repository)
        )

    }

    @Provides
    @Singleton
    fun provideLocalUseCases(repository: MusicRepository): LocalUseCases {
        return LocalUseCases(

            likeSong = LikeSong(repository),
            unlikeSong = UnlikeSong(repository),
            getLikedSongs = GetLikedSongs(repository),
            isLiked = IsLiked(repository),

            addSongToHistory = AddSongToHistory(repository),
            addArtistToHistory = AddArtistToHistory(repository),
            getHistory = GetHistory(repository),
            clearHistory = ClearHistory(repository),

            addSongToSearchHistory = AddSongToSearchHistory(repository),
            addArtistToSearchHistory = AddArtistToSearchHistory(repository),
            getSearchHistory = GetSearchHistory(repository),
            clearSearchHistory = ClearSearchHistory(repository)
        )
    }


    @Provides
    @Singleton
    fun providePlayerPreferencesManager(
        @ApplicationContext context: Context
    ): PlayerPreferencesManager = PlayerPreferencesManagerImpl(context)

    @Provides
    @Singleton
    fun provideLastSongUseCases(manager: PlayerPreferencesManager): LastSongUseCases {
        return LastSongUseCases(
            saveLastSong = SaveLastSong(manager),
            readLastSong = ReadLastSong(manager)
        )
    }

}