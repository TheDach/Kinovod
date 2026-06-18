package com.thedach.kinovod.domain.usecase.profile

import com.thedach.kinovod.domain.repository.ProfileRepository
import javax.inject.Inject

class RefreshUserDataUseCase @Inject constructor(
    private val repository: ProfileRepository
) {

    suspend operator fun invoke(
        userId: Int
    ) = repository.refreshUserData(userId)
}