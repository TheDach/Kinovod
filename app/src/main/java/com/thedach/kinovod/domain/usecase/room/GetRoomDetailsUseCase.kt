package com.thedach.kinovod.domain.usecase.room

import com.thedach.kinovod.domain.model.room.Room
import com.thedach.kinovod.domain.repository.RoomRepository
import javax.inject.Inject

class GetRoomDetailsUseCase @Inject constructor(
    private val repository: RoomRepository
) {

    suspend operator fun invoke(
        roomId: Int,
        userId: Int
    ) : Room = repository.getRoomDetails(roomId, userId)
}