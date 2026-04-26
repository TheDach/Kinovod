package com.thedach.kinovod.domain

class DeleteAccountUseCase(
    private val repository: AuthenticationRepository
) {
    suspend operator fun invoke (
        userId: Int
    ) = repository.deleteAccount(userId)
}