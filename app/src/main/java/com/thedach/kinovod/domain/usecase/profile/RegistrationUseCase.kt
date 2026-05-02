package com.thedach.kinovod.domain.usecase.profile

import com.thedach.kinovod.domain.repository.AuthenticationRepository

class RegistrationUseCase(
    private val repository: AuthenticationRepository
) {

    suspend operator fun invoke(
        username: String,
        userTag: String,
        email: String,
        password: String
    ) = repository.registration(username, userTag, email, password)
}