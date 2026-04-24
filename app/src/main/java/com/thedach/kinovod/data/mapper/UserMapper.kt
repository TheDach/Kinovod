package com.thedach.kinovod.data.mapper

import com.thedach.kinovod.data.network.model.FriendDto
import com.thedach.kinovod.data.network.model.UserDto
import com.thedach.kinovod.domain.model.Friend
import com.thedach.kinovod.domain.model.User

class UserMapper {

    fun mapUserToDomain(dto: UserDto): User {
        return User(
            userId = dto.userId,
            username = dto.username,
            userTag = dto.userTag,
            email = dto.email,
            avatar = dto.avatar,
            watchedList = dto.watchedList ?: emptyList(),
            wishList = dto.wishList ?: emptyList(),
            friends = dto.friends?.map { mapFriendToDomain(it) } ?: emptyList()
        )
    }

    private fun mapFriendToDomain(dto: FriendDto): Friend {
        return Friend(
            userId = dto.userId,
            username = dto.username,
            avatar = dto.avatar,
        )
    }

    // Обратный маппинг (если нужно отправлять на сервер)
    fun mapUserToDto(domain: User): UserDto {
        return UserDto(
            userId = domain.userId,
            username = domain.username,
            userTag = domain.userTag,
            email = domain.email,
            avatar = domain.avatar,
            watchedList = domain.watchedList ?: emptyList(),
            wishList = domain.wishList ?: emptyList(),
            friends = domain.friends?.map { mapFriendToDto(it) } ?: emptyList()
        )
    }

    private fun mapFriendToDto(domain: Friend): FriendDto {
        return FriendDto(
            userId = domain.userId,
            username = domain.username,
            avatar = domain.avatar,
        )
    }
}