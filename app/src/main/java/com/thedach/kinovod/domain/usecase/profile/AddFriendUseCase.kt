package com.thedach.kinovod.domain.usecase.profile

import com.thedach.kinovod.domain.repository.ProfileRepository

class AddFriendUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(
        userId: Int,
        friendTag: String
    ) = repository.addFriend(userId, friendTag)
}