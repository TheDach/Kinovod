package com.thedach.network

import com.thedach.kinovod.data.network.model.LoginRequestDto
import com.thedach.kinovod.data.network.model.RegisterRequestDto
import com.thedach.kinovod.data.network.model.ReviewResponseDto
import com.thedach.kinovod.data.network.model.UserResponseDto
import com.thedach.network.models.MovieResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    //https://api.poiskkino.dev/v1.5/movie?
    // token=&
    // limit=30&selectFields=id,name,rating,ageRating,movieLength,genres,description,videos,persons,year,poster&
    // sortField=votes.kp&sortType=-1&rating.kp=7-10&next=...

    @GET("movies")
    suspend fun getMovies(
        @Query(QUERY_PARAM_SELECT_FIELDS) selectFields: String? = null,
        @Query(QUERY_PARAM_RATING_KP) ratingKp: String? = DEFAULT_RATING_KP,
        @Query(QUERY_PARAM_GENRES_NAME) genreName: String? = null,
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


    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequestDto
    ) : UserResponseDto

    @POST("auth/register")
    suspend fun registration(
        @Body request: RegisterRequestDto
    ) : UserResponseDto

    companion object {
        private const val QUERY_PARAM_SELECT_FIELDS = "selectFields"
        private const val QUERY_PARAM_RATING_KP = "rating.kp"
        private const val QUERY_PARAM_GENRES_NAME = "genres.name"
        private const val QUERY_PARAM_NEXT_MOVIES = "next"
        private const val QUERY_PARAM_MOVIE_ID = "movieId"

        private const val DEFAULT_RATING_KP = "7-10"
    }
}