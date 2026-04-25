package com.thedach.kinovod.domain

class GetMovieListUseCase(
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