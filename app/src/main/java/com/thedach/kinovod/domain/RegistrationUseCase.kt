package com.thedach.kinovod.domain

import com.thedach.kinovod.data.repository.AuthenticationRepositoryImpl

class RegistrationUseCase(
    private val repository: AuthenticationRepository
) {

    suspend operator fun invoke(
        username: String,
        email: String,
        password: String
    ) = repository.registration(username, email, password)
}