package com.thedach.kinovod.domain.model

data class Review(
    val id: Int,
    val author: String,
    val typeRating: String,
    val review: String
)
