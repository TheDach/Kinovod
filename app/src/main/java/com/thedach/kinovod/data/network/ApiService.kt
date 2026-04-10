package com.thedach.network

import com.thedach.network.models.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    //https://api.poiskkino.dev/v1.5/movie?
    // token=&
    // limit=30&selectFields=id,name,rating,ageRating,movieLength,genres,description,videos,persons,year,poster&
    // sortField=votes.kp&sortType=-1&rating.kp=7-10

    @GET("movie")
    suspend fun getMovies(
        @Query(QUERY_PARAM_API_TOKEN) token: String = API_TOKEN,
        @Query(QUERY_PARAM_LIMIT) limit: Int = DEFAULT_LIMIT,
        @Query(QUERY_PARAM_SELECT_FIELDS) selectFields: String = DEFAULT_SELECT_FIELDS,
        @Query(QUERY_PARAM_SORT_FIELD) sortField: String = DEFAULT_SORT_FIELD,
        @Query(QUERY_PARAM_SORT_TYPE) sortType: Int = DEFAULT_SORT_TYPE,
        @Query(QUERY_PARAM_RATING_KP) ratingKp: String = DEFAULT_RATING_KP,
        @Query(QUERY_PARAM_GENRES_NAME) genreName: String = DEFAULT_GENRES_NAME
    ): MovieResponse

    suspend fun getReviews()

    companion object {
        private const val QUERY_PARAM_API_TOKEN = "api_token"
        private const val QUERY_PARAM_LIMIT = "limit"
        private const val QUERY_PARAM_SELECT_FIELDS = "selectFields"
        private const val QUERY_PARAM_SORT_TYPE = "sortType"
        private const val QUERY_PARAM_SORT_FIELD = "sortField"
        private const val QUERY_PARAM_RATING_KP = "rating.kp"
        private const val QUERY_PARAM_GENRES_NAME = "genres.name"


        const val API_TOKEN = ""
        const val DEFAULT_LIMIT = 30
        const val DEFAULT_SELECT_FIELDS = "id,name,rating,ageRating,movieLength,genres,description,videos,persons,year,poster"
        const val DEFAULT_SORT_FIELD = "votes.kp"
        const val DEFAULT_SORT_TYPE = -1
        const val DEFAULT_RATING_KP = "7-10"
        const val DEFAULT_GENRES_NAME = ""
    }
}