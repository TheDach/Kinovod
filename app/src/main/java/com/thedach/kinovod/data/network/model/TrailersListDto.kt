package com.thedach.network.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TrailersListDto(
    @SerializedName("trailers")
    @Expose
    val trailers: List<TrailerDto>
)
