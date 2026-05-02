package com.thedach.kinovod.data.network.model.room.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MatchResponseDto(

    @SerialName("voters")
    val voters: List<Int>, // Кто голосовал за фильм
    @SerialName("count")
    val count: Int // Количество голосов
)
