package com.example.musicapp.domain.model.response

import com.example.musicapp.domain.model.Artist
import com.google.gson.annotations.SerializedName

data class ArtistResponse(
    @SerializedName("headers")
    val headers: Headers? = null,
    @SerializedName("results")
    val results: List<Artist>
) {
    data class Headers(
        @SerializedName("code")
        val code: Int? = null,
        @SerializedName("error_message")
        val errorMessage: String? = null,
        @SerializedName("results_count")
        val resultsCount: Int? = null,
        @SerializedName("status")
        val status: String? = null,
        @SerializedName("warnings")
        val warnings: String? = null
    )


}