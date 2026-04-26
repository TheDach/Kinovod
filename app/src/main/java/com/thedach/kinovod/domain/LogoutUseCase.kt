package com.thedach.kinovod.domain

class LogoutUseCase(
    private val repository: AuthenticationRepository
) {

    suspend operator fun invoke(
        userId: Int
    ) = repository.logout(userId)
}