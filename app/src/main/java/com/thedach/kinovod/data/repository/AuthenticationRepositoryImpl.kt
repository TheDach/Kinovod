package com.thedach.kinovod.data.repository

import com.thedach.kinovod.data.mapper.UserMapper
import com.thedach.kinovod.data.network.model.LoginRequestDto
import com.thedach.kinovod.data.network.model.RegisterRequestDto
import com.thedach.kinovod.domain.AuthenticationRepository
import com.thedach.kinovod.domain.model.User
import com.thedach.network.ApiFactory

class AuthenticationRepositoryImpl: AuthenticationRepository {

    private val apiService = ApiFactory.apiService
    private val userMapper = UserMapper()

    override suspend fun login(
        email: String,
        password: String
    ): User {
        return try {

            val response = apiService.login(
                LoginRequestDto (email, password)
            )

            userMapper.mapUserToDomain(response.user)

        } catch (ex: Exception) {
            ex.printStackTrace()
            throw ex
        }


    }

    override suspend fun registration(
        username: String,
        email: String,
        password: String
    ): User {
        return try {

            val response = apiService.registration(
                RegisterRequestDto (username, email, password)
            )

            userMapper.mapUserToDomain(response.user)

        } catch (ex: Exception) {
            ex.printStackTrace()
            throw ex
        }
    }
}