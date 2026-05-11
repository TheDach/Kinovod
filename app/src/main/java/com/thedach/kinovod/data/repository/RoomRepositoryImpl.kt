package com.thedach.kinovod.data.repository

import android.util.Log
import com.thedach.kinovod.data.mapper.RoomMapper
import com.thedach.kinovod.data.network.model.room.request.AddMembersRequestDto
import com.thedach.kinovod.data.network.model.room.request.AddSuggestionsRequestDto
import com.thedach.kinovod.data.network.model.room.request.AddVotesRequestDto
import com.thedach.kinovod.data.network.model.room.request.CreateRoomRequestDto
import com.thedach.kinovod.domain.model.room.CreateRoom
import com.thedach.kinovod.domain.model.room.Room
import com.thedach.kinovod.domain.repository.RoomRepository
import com.thedach.network.ApiFactory

class RoomRepositoryImpl : RoomRepository {

    private val mapperRoom = RoomMapper()
    private val apiService = ApiFactory.apiService

    override suspend fun getUserRooms(userId: Int): List<Room> {
        return try {

            val response = apiService.getAllRoomForUser(userId)
            mapperRoom.mapUserRoomsDtoToDomain(response)

        } catch (ex: Exception) {
            ex.printStackTrace()
            throw Exception("Failed to get user rooms: ${ex.message}")
        }
    }

    override suspend fun getRoomDetails(
        roomId: Int,
        userId: Int
    ): Room {
        return try {
            val response = apiService.getRoomDetails(
                userId = userId,
                roomId = roomId
            )

            if (response.message == null) {
                mapperRoom.mapRoomDetailsDtoToDomain(response)
                    ?: throw Exception("Room not found")
            } else {
                throw Exception(response.message)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            throw Exception("Failed to get room details: ${ex.message}")
        }
    }

    override suspend fun createRoom(
        userId: Int,
        createRoom: CreateRoom
    ): Room {
        return try {
            val request = CreateRoomRequestDto(
                name = createRoom.name,
                description = createRoom.description,
                expiresAt = createRoom.expiresAt,
                maxMembers = createRoom.maxMembers,
                maxSuggestionsPerUser = createRoom.maxSuggestionsPerUser,
                votingType = createRoom.votingType.value,
                votingAnonymous = createRoom.votingAnonymous,
                votingShowResults = createRoom.votingShowResults,
                votingAllowChangingVote = createRoom.votingAllowChangingVote,
                useWishlists = createRoom.useWishlists,
                useWatchedLists = createRoom.useWatchedLists,
                genres = createRoom.genres,
                yearRangeMin = createRoom.yearRangeMin,
                yearRangeMax = createRoom.yearRangeMax,
                ratingRangeMin = createRoom.ratingRangeMin,
                ratingRangeMax = createRoom.ratingRangeMax,
                movieType = createRoom.movieType,
                countries = createRoom.countries,
                durationRangeMin = createRoom.durationRangeMin,
                durationRangeMax = createRoom.durationRangeMax,
                initialMembers = createRoom.initialMembers
            )

            val response = apiService.createNewRoom(
                userId = userId,
                request = request
            )

            if (response.message == null) {
                mapperRoom.mapRoomDetailsDtoToDomain(response)
                    ?: throw Exception("Failed to create room")
            } else {
                throw Exception(response.message)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            throw Exception("Failed to create room: ${ex.message}")
        }
    }

    override suspend fun addMembersToRoom(
        userId: Int,
        roomId: Int,
        userIds: List<Int>
    ) {
        try {
            val response = apiService.addMembersToRoom(
                roomId = roomId,
                userId = userId,
                request = AddMembersRequestDto(userIds = userIds)
            )


            Log.d("addMembersToRoom: ", response)
        } catch (ex: Exception) {
            ex.printStackTrace()
            throw Exception("Failed to add members: ${ex.message}")
        }
    }

    override suspend fun addUserVotes(
        userId: Int,
        roomId: Int,
        movieIds: List<Int>
    ) {
        try {
            val response = apiService.addUserVotes(
                roomId = roomId,
                userId = userId,
                request = AddVotesRequestDto(movieIds = movieIds)
            )

            Log.d("addUserVotes: ", response)
        } catch (ex: Exception) {
            ex.printStackTrace()
            throw Exception("Failed to add votes: ${ex.message}")
        }
    }

    override suspend fun addRoomSuggestions(
        userId: Int,
        roomId: Int,
        movieIds: List<Int>
    ) {
        try {
            val response = apiService.addRoomSuggestions(
                roomId = roomId,
                userId = userId,
                request = AddSuggestionsRequestDto(movieIds = movieIds)
            )

            Log.d("addRoomSuggestions: ", response)
        } catch (ex: Exception) {
            ex.printStackTrace()
            throw Exception("Failed to add suggestions: ${ex.message}")
        }
    }

    override suspend fun removeUserFromRoom(
        userId: Int,
        roomId: Int,
        userToRemoveId: Int
    ) {
        try {
            val response = apiService.removeUserFromRoom(
                roomId = roomId,
                userIdToRemove = userToRemoveId,
                userId = userId
            )

            Log.d("removeUserFromRoom: ", response)
        } catch (ex: Exception) {
            ex.printStackTrace()
            throw Exception("Failed to remove user from room: ${ex.message}")
        }
    }

    override suspend fun deleteRoom(userId: Int, roomId: Int) {
        try {
            val response = apiService.deleteRoom(
                roomId = roomId,
                userId = userId
            )

            Log.d("deleteRoom: ", response)
        } catch (ex: Exception) {
            ex.printStackTrace()
            throw Exception("Failed to delete room: ${ex.message}")
        }
    }
}