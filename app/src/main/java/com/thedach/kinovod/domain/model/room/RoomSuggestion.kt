package com.thedach.kinovod.domain.model.room

data class RoomSuggestion(
    val suggestionId: Int,
    val movieId: Int,
    val suggestedBy: Int?,
    val suggestedByUsername: String?,
    val voters: List<Int>
)
