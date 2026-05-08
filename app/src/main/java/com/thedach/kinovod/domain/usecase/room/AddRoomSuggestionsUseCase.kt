package com.thedach.kinovod.domain.usecase.room

import com.thedach.kinovod.domain.repository.RoomRepository

class AddRoomSuggestionsUseCase(
    private val repository: RoomRepository
) {

    suspend operator fun invoke(
        userId: Int, roomId: Int, movieIds: List<Int>
    ) = repository.addRoomSuggestions(userId, roomId, movieIds)
}