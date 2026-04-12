package com.thedach.kinovod.data.network.model

data class MovieRequestParams(
    val selectFields: String,
    val ratingKp: String,
    val genreName: String
)
