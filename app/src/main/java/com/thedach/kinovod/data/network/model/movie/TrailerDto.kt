package com.thedach.network.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TrailerDto(
    @SerializedName("url")
    @Expose
    val url: String,

    @SerializedName("name")
    @Expose
    val name: String
)
