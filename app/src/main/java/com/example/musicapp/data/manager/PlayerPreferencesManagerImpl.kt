package com.example.musicapp.data.manager

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.musicapp.domain.manager.PlayerPreferencesManager
import com.example.musicapp.domain.model.Song
import kotlinx.coroutines.flow.Flow
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.map

class PlayerPreferencesManagerImpl(
    private val context: Context
): PlayerPreferencesManager {
    override suspend fun saveLastSong(song: Song) {
        context.playerDataStore.edit { prefs ->
            prefs[PreferencesKeys.SONG_ID] = song.id
            prefs[PreferencesKeys.SONG_NAME] = song.name
            prefs[PreferencesKeys.SONG_ARTIST] = song.artistName
            prefs[PreferencesKeys.SONG_IMAGE] = song.image ?: ""
            prefs[PreferencesKeys.SONG_AUDIO] = song.audio ?: ""
            prefs[PreferencesKeys.SONG_DURATION] = song.duration
            prefs[PreferencesKeys.ARTIST_ID] = song.artistId
        }
    }

    override fun getLastSong(): Flow<Song?> {
        return context.playerDataStore.data.map { prefs ->
            val id = prefs[PreferencesKeys.SONG_ID] ?: return@map null
            Song(
                id = id,
                name = prefs[PreferencesKeys.SONG_NAME] ?: "",
                artistName = prefs[PreferencesKeys.SONG_ARTIST] ?: "",
                image = prefs[PreferencesKeys.SONG_IMAGE],
                audio = prefs[PreferencesKeys.SONG_AUDIO],
                duration = prefs[PreferencesKeys.SONG_DURATION] ?: 0,
                artistId = prefs[PreferencesKeys.ARTIST_ID] ?: "",
                albumId = "", albumImage = "", albumName = "",
                 artistIdstr = "", audiodownload = "",
                audiodownloadAllowed = false, contentIdFree = false,
                licenseCcurl = "", position = 0, prourl = "",
                releasedate = "", shareurl = "", shorturl = ""
            )
        }
    }

}

private val Context.playerDataStore: DataStore<Preferences> by preferencesDataStore(name = "player_prefs")
private object PreferencesKeys {
    val SONG_ID = stringPreferencesKey("song_id")
    val SONG_NAME = stringPreferencesKey("song_name")
    val SONG_ARTIST = stringPreferencesKey("song_artist")
    val SONG_IMAGE = stringPreferencesKey("song_image")
    val SONG_AUDIO = stringPreferencesKey("song_audio")
    val SONG_DURATION = intPreferencesKey("song_duration")
    val ARTIST_ID = stringPreferencesKey("artist_id")
}