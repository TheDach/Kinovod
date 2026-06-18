package com.thedach.kinovod.domain.usecase.room

import com.thedach.kinovod.domain.repository.RoomRepository
import javax.inject.Inject

class AddUserVotesUseCase @Inject constructor(
    private val repository: RoomRepository
) {

    suspend operator fun invoke(
        userId: Int,
        roomId: Int,
        movieIds: List<Int>
    ) = repository.addUserVotes(userId, roomId, movieIds)
}