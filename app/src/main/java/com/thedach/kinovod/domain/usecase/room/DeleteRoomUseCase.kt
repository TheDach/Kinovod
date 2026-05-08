package com.thedach.kinovod.domain.usecase.room

import com.thedach.kinovod.domain.repository.RoomRepository

class DeleteRoomUseCase(
    private val repository: RoomRepository
) {

    suspend operator fun invoke(
        userId: Int,
        roomId: Int
    ) = repository.deleteRoom(userId, roomId)
}