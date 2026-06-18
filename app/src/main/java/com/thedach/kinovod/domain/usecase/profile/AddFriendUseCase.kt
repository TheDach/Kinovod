package com.thedach.kinovod.domain.usecase.profile

import com.thedach.kinovod.domain.repository.ProfileRepository
import javax.inject.Inject

class AddFriendUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(
        userId: Int,
        friendTag: String
    ) = repository.addFriend(userId, friendTag)
}