package com.thedach.kinovod.data.network.model.movie

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReviewResponseDto(
    @SerializedName("docs")
    @Expose
    val reviews: List<ReviewDto>? = null
)
