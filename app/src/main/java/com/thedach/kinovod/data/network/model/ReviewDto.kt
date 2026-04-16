package com.thedach.kinovod.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReviewDto(
    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("author")
    @Expose
    val author: String,

    @SerializedName("type")
    @Expose
    val typeRating: String,

    @SerializedName("review")
    @Expose
    val review: String
)