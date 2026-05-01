package com.thedach.network.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MovieRatingDto(
    @SerializedName("kp")
    @Expose
    val kp: Double,

    @SerializedName("imdb")
    @Expose
    val imdb: Double
)
