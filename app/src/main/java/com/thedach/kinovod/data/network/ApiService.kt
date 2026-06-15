package com.thedach.network

import com.thedach.kinovod.data.network.model.AddFriendRequestDto
import com.thedach.kinovod.data.network.model.BaseResponseDto
import com.thedach.kinovod.data.network.model.LoginRequestDto
import com.thedach.kinovod.data.network.model.RegisterRequestDto
import com.thedach.kinovod.data.network.model.SimpleMessageResponse
import com.thedach.kinovod.data.network.model.SyncUserDataRequest
import com.thedach.kinovod.data.network.model.UserResponseDto
import com.thedach.kinovod.data.network.model.movie.ReviewResponseDto
import com.thedach.kinovod.data.network.model.room.request.AddMembersRequestDto
import com.thedach.kinovod.data.network.model.room.request.AddSuggestionsRequestDto
import com.thedach.kinovod.data.network.model.room.request.AddVotesRequestDto
import com.thedach.kinovod.data.network.model.room.request.CreateRoomRequestDto
import com.thedach.kinovod.data.network.model.room.response.RoomDetailsResponseDto
import com.thedach.kinovod.data.network.model.room.response.UserRoomsResponseDto
import com.thedach.network.models.MovieResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // ==================== MOVIES ===================

    @GET("movies")
    suspend fun getMovies(
        @Query(QUERY_PARAM_SELECT_FIELDS) selectFields: String? = null,
        @Query(QUERY_PARAM_RATING_KP) ratingKp: String? = DEFAULT_RATING_KP,
        @Query(QUERY_PARAM_GENRES_NAME) genreName: List<String>? = null,
        @Query(QUERY_PARAM_NEXT_MOVIES) nextMovies: String? = null
    ): MovieResponse

    @GET("movies")
    suspend fun getMoviesById(
        @Query(QUERY_PARAM_MOVIE_ID) movieId: List<Int>
    ): MovieResponse


    @GET("reviewsAPI")
    suspend fun getReviews(
        @Query(QUERY_PARAM_MOVIE_ID) movieId: Int
    ): ReviewResponseDto


    // ============== AUTH AND PROFILE ===============


    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequestDto
    ): UserResponseDto

    @POST("auth/register")
    suspend fun registration(
        @Body request: RegisterRequestDto
    ): UserResponseDto

    @POST("auth/deleteAccount")
    suspend fun deleteAccount(
        @Body userId: Int
    )

    @POST("auth/logout")
    suspend fun logout(
        @Body userId: Int
    )

    @GET("/user/profile/refresh_user_data")
    suspend fun refreshUserData(
        @Query(QUERY_PARAM_USER_ID) userId: Int
    ): UserResponseDto

    @POST("/user/profile/add_friend")
    suspend fun addFriend(
        @Body request: AddFriendRequestDto
    ): UserResponseDto

    // ==================== SYNC =====================

    @POST("user/sync")
    suspend fun syncUserData(
        @Body request: SyncUserDataRequest
    ): BaseResponseDto<Response<UInt>>


    // ==================== ROOMS ====================

    @GET("room/all_room_details")
    suspend fun getAllRoomForUser(
        @Query(QUERY_PARAM_USER_ID) userId: Int
    ): UserRoomsResponseDto

    @GET("room/details")
    suspend fun getRoomDetails(
        @Query(QUERY_PARAM_USER_ID) userId: Int,
        @Query(QUERY_PARAM_ROOM_ID) roomId: Int
    ): RoomDetailsResponseDto

    @POST("room/create")
    suspend fun createNewRoom(
        @Query(QUERY_PARAM_USER_ID) userId: Int,
        @Body request: CreateRoomRequestDto
    ): RoomDetailsResponseDto

    @POST("room/{roomId}/members")
    suspend fun addMembersToRoom(
        @Path(QUERY_PARAM_ROOM_ID) roomId: Int,
        @Query(QUERY_PARAM_USER_ID) userId: Int,
        @Body request: AddMembersRequestDto
    ): Response<String>

    @POST("room/{roomId}/votes")
    suspend fun addUserVotes(
        @Path(QUERY_PARAM_ROOM_ID) roomId: Int,
        @Query(QUERY_PARAM_USER_ID) userId: Int,
        @Body request: AddVotesRequestDto
    ): Response<String>


    @POST("room/{roomId}/suggestions")
    suspend fun addRoomSuggestions(
        @Path(QUERY_PARAM_ROOM_ID) roomId: Int,
        @Query(QUERY_PARAM_USER_ID) userId: Int,
        @Body request: AddSuggestionsRequestDto
    ): Response<String>

    @DELETE("room/{roomId}/members/{userIdToRemove}")
    suspend fun removeUserFromRoom(
        @Path(QUERY_PARAM_ROOM_ID) roomId: Int,
        @Path(QUERY_PARAM_USER_ID_TO_REMOVE) userIdToRemove: Int,
        @Query(QUERY_PARAM_USER_ID) userId: Int
    ): Response<String>

    @DELETE("room/{roomId}")
    suspend fun deleteRoom(
        @Path(QUERY_PARAM_ROOM_ID) roomId: Int,
        @Query(QUERY_PARAM_USER_ID) userId: Int
    ): Response<String>


    companion object {
        private const val QUERY_PARAM_SELECT_FIELDS = "selectFields"
        private const val QUERY_PARAM_RATING_KP = "rating.kp"
        private const val QUERY_PARAM_GENRES_NAME = "genres.name"
        private const val QUERY_PARAM_NEXT_MOVIES = "next"
        private const val QUERY_PARAM_MOVIE_ID = "movieId"

        private const val QUERY_PARAM_USER_ID = "userId"
        private const val QUERY_PARAM_USER_ID_TO_REMOVE = "userIdToRemove"
        private const val QUERY_PARAM_ROOM_ID = "roomId"

        private const val DEFAULT_RATING_KP = "7-10"
    }
}