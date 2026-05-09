package com.thedach.kinovod.domain.model.room

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RoomSuggestion(
    val suggestionId: Int,
    val movieId: Int,
    val suggestedBy: Int?,
    val suggestedByUsername: String?,
    val voters: List<Int>
) : Parcelable
