package com.example.musicapp.data.local.mapper

import com.example.musicapp.data.local.entity.ArtistCategory
import com.example.musicapp.data.local.entity.ArtistEntity
import com.example.musicapp.domain.model.Artist

object ArtistMapper {
    fun Artist.toEntity(category: ArtistCategory) = ArtistEntity(
        id = id,
        category = category,
        name = name,
        image = image,
        shareurl = shareurl,
        shorturl = shorturl,
        website = website,
        joindate = joindate,
        tags = musicinfo?.tags?.filterNotNull() ?: emptyList()
    )

    fun ArtistEntity.toDomain() = Artist(
        id = id,
        name = name,
        image = image,
        shareurl = shareurl,
        shorturl = shorturl,
        website = website,
        joindate = joindate,
        musicinfo = Artist.Musicinfo(tags = tags)
    )
}