package com.thedach.kinovod.data.network.model.room.response

data class MatchResponseDto(
    val voters: List<Int>, // Кто голосовал за фильм
    val count: Int // Количество голосов
)
