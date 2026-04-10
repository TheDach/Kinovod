package com.thedach.network.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PosterDto(
    @SerializedName("url")
    @Expose
    val url: String
)
