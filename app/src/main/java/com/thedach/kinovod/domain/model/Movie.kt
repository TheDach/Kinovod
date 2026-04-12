package com.thedach.kinovod.domain.model

data class Movie(
    val id: Int,
    val name: String,
    val year: Int,
    val description: String,
    val movieLengthMin: Int,
    val movieLengthHour: Int,
    val ageRating: Int?,
    val poster: String,
    val rating: MovieRating,
    val trailers: List<Trailer>,
    val persons: List<Person>,
    val genres: List<String>?
)
