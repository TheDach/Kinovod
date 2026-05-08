package com.thedach.kinovod.domain.usecase.room

import com.thedach.kinovod.domain.repository.RoomRepository

class AddUserVotesUseCase(
    private val repository: RoomRepository
) {

    suspend operator fun invoke(
        userId: Int,
        roomId: Int,
        movieIds: List<Int>
    ) = repository.addUserVotes(userId, roomId, movieIds)
}