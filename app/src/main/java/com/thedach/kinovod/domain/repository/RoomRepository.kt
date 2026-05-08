package com.thedach.kinovod.domain.repository

import com.thedach.kinovod.domain.model.room.CreateRoom
import com.thedach.kinovod.domain.model.room.Room

interface RoomRepository {

    suspend fun getUserRooms(userId: Int): List<Room>
    suspend fun getRoomDetails(roomId:Int, userId: Int): Room

    suspend fun createRoom(userId: Int, createRoom: CreateRoom): Room

    suspend fun addMembersToRoom(userId: Int, roomId: Int, userIds: List<Int>)
    suspend fun addUserVotes(userId: Int, roomId: Int, movieIds: List<Int>)
    suspend fun addRoomSuggestions(userId: Int, roomId: Int, movieIds: List<Int>)

    suspend fun removeUserFromRoom(userId: Int, roomId: Int, userToRemoveId: Int)
    suspend fun deleteRoom(userId: Int, roomId: Int)
}