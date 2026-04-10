package com.thedach.network.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GenresDto(
    @SerializedName("name")
    @Expose
    val name: String,
)
