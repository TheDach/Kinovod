package com.thedach.kinovod.data.network.model.room.response

data class SuggestionResponseDto(
    val suggestionId: Int,
    val movieId: Int,
    val suggestedBy: Int?,
    val suggestedByUsername: String?,
    val voters: List<Int> // Список ID пользователей, проголосовавших за этот фильм
)
