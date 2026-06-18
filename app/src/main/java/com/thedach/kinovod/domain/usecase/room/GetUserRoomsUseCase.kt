package com.thedach.kinovod.domain.usecase.room

import com.thedach.kinovod.domain.repository.RoomRepository
import javax.inject.Inject

class GetUserRoomsUseCase @Inject constructor(
    private val repository: RoomRepository
) {
    suspend operator fun invoke(
        userId: Int
    )  = repository.getUserRooms(userId)
}