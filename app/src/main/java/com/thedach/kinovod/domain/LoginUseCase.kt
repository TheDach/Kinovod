package com.thedach.kinovod.domain

class LoginUseCase(
    private val repository: AuthenticationRepository
) {

    suspend operator fun invoke(
        email: String,
        password: String
    ) = repository.login(email, password)
}