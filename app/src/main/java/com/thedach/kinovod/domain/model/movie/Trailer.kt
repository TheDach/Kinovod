package com.thedach.kinovod.domain.model.movie

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Trailer(
    val url: String,
    val name: String
) : Parcelable
