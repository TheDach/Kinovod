package com.thedach.kinovod.domain.model.movie

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieRating(
    val kp: String,
    val imdb: String
): Parcelable
