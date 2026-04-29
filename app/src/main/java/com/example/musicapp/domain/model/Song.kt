package com.example.musicapp.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Song(
    @SerializedName("album_id")
    val albumId: String,
    @SerializedName("album_image")
    val albumImage: String,
    @SerializedName("album_name")
    val albumName: String,
    @SerializedName("artist_id")
    val artistId: String,
    @SerializedName("artist_idstr")
    val artistIdstr: String,
    @SerializedName("artist_name")
    val artistName: String,
    @SerializedName("audio")
    val audio: String? = null,
    @SerializedName("audiodownload")
    val audiodownload: String,
    @SerializedName("audiodownload_allowed")
    val audiodownloadAllowed: Boolean,
    @SerializedName("content_id_free")
    val contentIdFree: Boolean,
    @SerializedName("duration")
    val duration: Int,
    @SerializedName("id")
    val id: String,
    @SerializedName("image")
    val image: String? = null,
    @SerializedName("license_ccurl")
    val licenseCcurl: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("position")
    val position: Int,
    @SerializedName("prourl")
    val prourl: String,
    @SerializedName("releasedate")
    val releasedate: String,
    @SerializedName("shareurl")
    val shareurl: String,
    @SerializedName("shorturl")
    val shorturl: String,
    @SerializedName("waveform")
    val waveform: String? = null
): Parcelable