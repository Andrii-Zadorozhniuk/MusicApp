package com.example.musicapp.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Artist(
    @SerializedName("id")
    val id: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("joindate")
    val joindate: String,
    @SerializedName("musicinfo")
    val musicinfo: Musicinfo? = null,
    @SerializedName("name")
    val name: String,
    @SerializedName("shareurl")
    val shareurl: String? = null,
    @SerializedName("shorturl")
    val shorturl: String? = null,
    @SerializedName("website")
    val website: String? = null
): Parcelable {
    @Parcelize
    data class Musicinfo(
        @SerializedName("description")
        val description: Description? = null,
        @SerializedName("tags")
        val tags: List<String?>? = null
    ): Parcelable {
        @Parcelize
        data class Description(
            @SerializedName("de")
            val de: String? = null,
            @SerializedName("en")
            val en: String? = null,
            @SerializedName("es")
            val es: String? = null,
            @SerializedName("fr")
            val fr: String? = null,
            @SerializedName("it")
            val `it`: String? = null,
            @SerializedName("ja")
            val ja: String? = null,
            @SerializedName("pl")
            val pl: String? = null,
            @SerializedName("pt")
            val pt: String? = null,
            @SerializedName("ru")
            val ru: String? = null
        ): Parcelable
    }
}