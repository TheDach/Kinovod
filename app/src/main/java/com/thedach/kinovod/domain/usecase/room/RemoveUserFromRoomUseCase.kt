package com.thedach.kinovod.domain.usecase.room

import com.thedach.kinovod.domain.repository.RoomRepository
import javax.inject.Inject

class RemoveUserFromRoomUseCase @Inject constructor(
    private val repository: RoomRepository
) {

    suspend operator fun invoke(
        userId: Int,
        roomId: Int,
        userToRemoveId: Int
    ) = repository.removeUserFromRoom(userId, roomId, userToRemoveId)
}