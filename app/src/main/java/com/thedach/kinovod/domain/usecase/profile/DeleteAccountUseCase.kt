package com.thedach.kinovod.domain.usecase.profile

import com.thedach.kinovod.domain.repository.AuthenticationRepository
import javax.inject.Inject

class DeleteAccountUseCase @Inject constructor(
    private val repository: AuthenticationRepository
) {
    suspend operator fun invoke (
        userId: Int
    ) = repository.deleteAccount(userId)
}