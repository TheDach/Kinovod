package com.thedach.kinovod.domain.usecase.room

import com.thedach.kinovod.domain.repository.RoomRepository

class RemoveUserFromRoomUseCase(
    private val repository: RoomRepository
) {

    suspend operator fun invoke(
        userId: Int,
        roomId: Int,
        userToRemoveId: Int
    ) = repository.removeUserFromRoom(userId, roomId, userToRemoveId)
}