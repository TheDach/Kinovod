package com.thedach.kinovod.domain.model.movie

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieSelectionConfig(
    val movieFilters: Settings? = null,
    val mode: SearchMode = SearchMode.ALL_MOVIES,
    val roomId: Int? = null
) : Parcelable
