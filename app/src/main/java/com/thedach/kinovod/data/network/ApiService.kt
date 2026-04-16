package com.thedach.network

import com.thedach.kinovod.data.network.model.ReviewResponse
import com.thedach.network.models.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    //https://api.poiskkino.dev/v1.5/movie?
    // token=&
    // limit=30&selectFields=id,name,rating,ageRating,movieLength,genres,description,videos,persons,year,poster&
    // sortField=votes.kp&sortType=-1&rating.kp=7-10&next=...

    @GET("movies")
    suspend fun getMovies(
        @Query(QUERY_PARAM_SELECT_FIELDS) selectFields: String? = null,
        @Query(QUERY_PARAM_RATING_KP) ratingKp: String? = null,
        @Query(QUERY_PARAM_GENRES_NAME) genreName: String? = null,
        @Query(QUERY_PARAM_NEXT_MOVIES) nextMovies: String? = null
    ): MovieResponse

    @GET("reviewsAPI")
    suspend fun getReviews(
        @Query(QUERY_PARAM_MOVIE_ID) movieId: Int
    ): ReviewResponse

    companion object {
        private const val QUERY_PARAM_SELECT_FIELDS = "selectFields"
        private const val QUERY_PARAM_RATING_KP = "rating.kp"
        private const val QUERY_PARAM_GENRES_NAME = "genres.name"
        private const val QUERY_PARAM_NEXT_MOVIES = "next"
        private const val QUERY_PARAM_MOVIE_ID = "movieId"
    }
}