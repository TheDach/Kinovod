package com.thedach.kinovod.domain.usecase.profile

import com.thedach.kinovod.domain.repository.AuthenticationRepository

class LoginUseCase(
    private val repository: AuthenticationRepository
) {

    suspend operator fun invoke(
        email: String,
        password: String
    ) = repository.login(email, password)
}