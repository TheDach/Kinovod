package com.thedach.kinovod.data.repository

import com.thedach.kinovod.data.mapper.UserMapper
import com.thedach.kinovod.data.network.model.LoginRequestDto
import com.thedach.kinovod.data.network.model.RegisterRequestDto
import com.thedach.kinovod.domain.AuthenticationRepository
import com.thedach.kinovod.domain.model.profile.User
import com.thedach.network.ApiFactory

class AuthenticationRepositoryImpl : AuthenticationRepository {

    private val apiService = ApiFactory.apiService
    private val userMapper = UserMapper()

    private var userRepository = UserRepository

    override suspend fun login(
        email: String,
        password: String
    ): User {
        return try {

            val response = apiService.login(
                LoginRequestDto(email, password)
            )

            userMapper.mapUserToDomain(response.user).also { saveUser(it) }

        } catch (ex: Exception) {
            ex.printStackTrace()
            throw ex
        }


    }

    override suspend fun registration(
        username: String,
        userTag: String,
        email: String,
        password: String
    ): User {
        return try {

            val response = apiService.registration(
                RegisterRequestDto(username, userTag, email, password)
            )

            userMapper.mapUserToDomain(response.user).also { saveUser(it)}

        } catch (ex: Exception) {
            ex.printStackTrace()
            throw ex
        }
    }

    override suspend fun logout(userId: Int) {
        try {

            apiService.logout(userId)

        } catch (ex: Exception) {
            ex.printStackTrace()
            throw ex
        }
    }

    override suspend fun deleteAccount(userId: Int) {
        try {

            apiService.deleteAccount(userId)

        } catch (ex: Exception) {
            ex.printStackTrace()
            throw ex
        }
    }

    private fun saveUser(user: User) {
        userRepository.setUser(user)
    }
}