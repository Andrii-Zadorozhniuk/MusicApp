package com.example.musicapp.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Album(
    @SerializedName("artist_id")
    val artistId: String = "",
    @SerializedName("artist_name")
    val artistName: String = "",
    @SerializedName("id")
    val id: String = "",
    @SerializedName("image")
    val image: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("releasedate")
    val releasedate: String = "",
    @SerializedName("shareurl")
    val shareurl: String = "",
    @SerializedName("shorturl")
    val shorturl: String = "",
    @SerializedName("zip")
    val zip: String = "",
    @SerializedName("zip_allowed")
    val zipAllowed: Boolean = false
) : Parcelable