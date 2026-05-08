package com.thedach.kinovod.domain.usecase.room

import com.thedach.kinovod.domain.model.room.CreateRoom
import com.thedach.kinovod.domain.model.room.Room
import com.thedach.kinovod.domain.repository.RoomRepository

class CreateRoomUseCase(
    private val repository: RoomRepository
) {

    suspend operator fun invoke (
        userId: Int,
        createRoom: CreateRoom
    ) : Room = repository.createRoom(userId, createRoom)
}