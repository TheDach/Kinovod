package com.thedach.kinovod.data.repository

import com.thedach.kinovod.data.mapper.UserMapper
import com.thedach.kinovod.data.network.model.AddFriendRequestDto
import com.thedach.kinovod.domain.model.profile.User
import com.thedach.kinovod.domain.repository.ProfileRepository
import com.thedach.network.ApiFactory

class ProfileRepositoryImpl : ProfileRepository {

    private val apiService = ApiFactory.apiService
    private val userMapper = UserMapper()

    override suspend fun addFriend(
        userId: Int,
        friendTag: String
    ) {
        try {

            val response = apiService.addFriend(
                AddFriendRequestDto(
                    userId = userId,
                    friendTag = friendTag
                )
            )
            saveUser( userMapper.mapUserToDomain(response.user) )

        } catch (ex: Exception) {
            ex.printStackTrace()
            throw ex
        }
    }

    override suspend fun refreshUserData(userId: Int) {
        try {

            val response = apiService.refreshUserData(userId)
            saveUser( userMapper.mapUserToDomain(response.user) )

        } catch (ex: Exception) {
            ex.printStackTrace()
            throw ex
        }
    }

    private fun saveUser(user: User) {
        UserRepository.setUser(user)
    }
}