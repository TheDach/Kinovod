package com.thedach.kinovod.data.network.model.room

data class MatchResponseDto(
    val voters: List<Int>, // Кто голосовал за фильм
    val count: Int // Количество голосов
)
