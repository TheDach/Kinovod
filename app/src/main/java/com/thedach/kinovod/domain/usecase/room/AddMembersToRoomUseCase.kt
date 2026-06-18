package com.thedach.kinovod.domain.usecase.room

import com.thedach.kinovod.domain.repository.RoomRepository
import javax.inject.Inject

class AddMembersToRoomUseCase @Inject constructor(
    private val repository: RoomRepository
) {

    suspend operator fun invoke(
        userId: Int,
        roomId: Int,
        userIds: List<Int>
    ) = repository.addMembersToRoom(userId, roomId, userIds)
}