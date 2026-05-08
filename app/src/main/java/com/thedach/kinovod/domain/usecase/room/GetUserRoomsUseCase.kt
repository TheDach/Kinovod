package com.thedach.kinovod.domain.usecase.room

import com.thedach.kinovod.domain.repository.RoomRepository

class GetUserRoomsUseCase(
    private val repository: RoomRepository
) {
    suspend operator fun invoke(
        userId: Int
    )  = repository.getUserRooms(userId)
}