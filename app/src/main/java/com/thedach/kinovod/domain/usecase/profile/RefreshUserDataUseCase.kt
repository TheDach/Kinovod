package com.thedach.kinovod.domain.usecase.profile

import com.thedach.kinovod.domain.repository.ProfileRepository

class RefreshUserDataUseCase(
    private val repository: ProfileRepository
) {

    suspend operator fun invoke(
        userId: Int
    ) = repository.refreshUserData(userId)
}