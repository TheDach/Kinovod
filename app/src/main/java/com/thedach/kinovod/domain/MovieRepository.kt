package com.thedach.kinovod.domain

import androidx.lifecycle.LiveData
import com.thedach.kinovod.domain.model.Movie
import com.thedach.kinovod.domain.model.Review

interface MovieRepository {

    suspend fun getMovieList(
        selectFields: String? = null,
        ratingKp: String? = null,
        genreName: String? = null
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