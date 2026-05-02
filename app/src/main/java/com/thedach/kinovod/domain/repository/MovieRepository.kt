package com.thedach.kinovod.domain.repository

import com.thedach.kinovod.domain.model.movie.Movie
import com.thedach.kinovod.domain.model.movie.Review

interface MovieRepository {

    suspend fun getMovieList(
        selectFields: String? = null,
        ratingKp: String? = null,
        genreName: List<String>? = null
    ): List<Movie>

    suspend fun getMovieListById(
        movieId: List<Int>
    ): List<Movie>

    suspend fun getReviewListMovie(
        movieId: Int
    ): List<Review>



    /*suspend fun loadNextPage(
        selectFields: String? = null,
        ratingKp: String? = null,
        genreName: String? = null
    ):*/
}