package com.thedach.kinovod.data.network.model.room.response

data class UserRoomsResponseDto(
    val message: String?,
    val rooms: List<RoomResponseDto>?
)
