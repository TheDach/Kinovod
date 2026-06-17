package com.thedach.kinovod.domain.usecase.movie

import com.thedach.kinovod.domain.repository.MovieRepository
import javax.inject.Inject

class GetMovieListUseCase @Inject constructor(
    private val repository: MovieRepository
) {

    suspend operator fun invoke(
        selectFields: String? = null,
        ratingKp: String? = null,
        genreName: List<String>? = null
    ) = repository.getMovieList(
        selectFields,
        ratingKp,
        genreName
    )
}