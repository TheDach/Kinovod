package com.thedach.kinovod.data.network.model.room.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SuggestionResponseDto(

    @SerialName("suggestionId")
    val suggestionId: Int,

    @SerialName("movieId")
    val movieId: Int,

    @SerialName("suggestedBy")
    val suggestedBy: Int?,

    @SerialName("suggestedByUsername")
    val suggestedByUsername: String?,

    @SerialName("voters")
    val voters: List<Int> // Список ID пользователей, проголосовавших за этот фильм
)
