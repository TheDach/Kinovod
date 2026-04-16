package com.thedach.kinovod.domain

import androidx.lifecycle.LiveData
import com.thedach.kinovod.domain.model.Movie

interface MovieRepository {

    suspend fun getMovieList(
        selectFields: String? = null,
        ratingKp: String? = null,
        genreName: String? = null
    ): List<Movie>

    /*suspend fun loadNextPage(
        selectFields: String? = null,
        ratingKp: String? = null,
        genreName: String? = null
    ):*/
}