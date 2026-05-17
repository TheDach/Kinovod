package com.thedach.kinovod.domain.model.movie

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieSelectionConfig(
    val movieFilters: Settings,
    val mode: SearchMode,
    val roomId: Int
) : Parcelable
