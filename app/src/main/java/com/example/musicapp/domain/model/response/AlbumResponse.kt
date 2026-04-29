package com.example.musicapp.domain.model.response


import com.example.musicapp.domain.model.Album
import com.google.gson.annotations.SerializedName

data class AlbumResponse(
    @SerializedName("headers")
    val headers: Headers = Headers(),
    @SerializedName("results")
    val results: List<Album> = listOf()
) {
    data class Headers(
        @SerializedName("code")
        val code: Int = 0,
        @SerializedName("error_message")
        val errorMessage: String = "",
        @SerializedName("next")
        val next: String = "",
        @SerializedName("results_count")
        val resultsCount: Int = 0,
        @SerializedName("status")
        val status: String = "",
        @SerializedName("warnings")
        val warnings: String = ""
    )


}