package com.thedach.kinovod.data.network.model.room

data class UserRoomsResponseDto(
    val message: String?,
    val rooms: List<RoomResponseDto>?
)
