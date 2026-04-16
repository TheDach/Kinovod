package com.thedach.network.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MovieResponse(

    @SerializedName("docs")
    @Expose
    val movies: List<MovieDto>? = null,

    @SerializedName("next")
    @Expose
    val nextMovies: String? = null,

    @SerializedName("hasNext")
    @Expose
    val hasNext: Boolean = false
)
