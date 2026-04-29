package com.example.musicapp.data.local.mapper

import com.example.musicapp.data.local.entity.SongCategory
import com.example.musicapp.data.local.entity.SongEntity
import com.example.musicapp.domain.model.Song

object SongMapper {
    fun Song.toEntity(category: SongCategory) = SongEntity(
        id = id,
        category = category,
        albumId = albumId,
        albumImage = albumImage,
        albumName = albumName,
        artistId = artistId,
        artistIdstr = artistIdstr,
        artistName = artistName,
        audio = audio,
        audiodownload = audiodownload,
        audiodownloadAllowed = audiodownloadAllowed,
        contentIdFree = contentIdFree,
        duration = duration,
        image = image,
        licenseCcurl = licenseCcurl,
        name = name,
        apiPosition = position,
        position = 0,
        prourl = prourl,
        releasedate = releasedate,
        shareurl = shareurl,
        shorturl = shorturl,
        waveform = waveform
    )

    fun SongEntity.toDomain() = Song(
        id = id,
        albumId = albumId,
        albumImage = albumImage,
        albumName = albumName,
        artistId = artistId,
        artistIdstr = artistIdstr,
        artistName = artistName,
        audio = audio,
        audiodownload = audiodownload,
        audiodownloadAllowed = audiodownloadAllowed,
        contentIdFree = contentIdFree,
        duration = duration,
        image = image,
        licenseCcurl = licenseCcurl,
        name = name,
        position = position,
        prourl = prourl,
        releasedate = releasedate,
        shareurl = shareurl,
        shorturl = shorturl,
        waveform = waveform
    )
}