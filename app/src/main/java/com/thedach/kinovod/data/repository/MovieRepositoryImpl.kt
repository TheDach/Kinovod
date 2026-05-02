package com.thedach.kinovod.data.repository

import com.thedach.kinovod.data.mapper.MovieMapper
import com.thedach.kinovod.data.mapper.ReviewMapper
import com.thedach.kinovod.domain.MovieRepository
import com.thedach.kinovod.domain.model.movie.Movie
import com.thedach.kinovod.domain.model.movie.Review
import com.thedach.network.ApiFactory

object MovieRepositoryImpl: MovieRepository {

    private val apiService = ApiFactory.apiService
    private val mapperMovie = MovieMapper()
    private val mapperReview = ReviewMapper()

    private var nextCursor: String? = null
    private var hasNext = true
    private var isFirstLoad = true

    override suspend fun getMovieList(
        selectFields: String?,
        ratingKp: String?,
        genreName: List<String>?
    ): List<Movie> {

        return try {
            val response = apiService.getMovies(
                selectFields = selectFields,
                ratingKp = ratingKp,
                genreName = genreName
            )

            response.movies?.map{ mapperMovie.mapMovieDtoToDomainModel(it) } ?: emptyList()

        } catch (ex: Exception) {
            ex.printStackTrace()
            throw ex
        }
    }

    override suspend fun getMovieListById(movieId: List<Int>): List<Movie> {
        return try {

            val response = apiService.getMoviesById(movieId)
            response.movies?.map { mapperMovie.mapMovieDtoToDomainModel(it) } ?: emptyList()

        } catch (ex : Exception) {
            ex.printStackTrace()
            throw ex
        }
    }

    override suspend fun getReviewListMovie(movieId: Int): List<Review> {
        return try {

            val response = apiService.getReviews(movieId)
            response.reviews?.map { mapperReview.mapReviewDtoToDomainModel(it) } ?: emptyList()

        } catch (ex: Exception) {
            ex.printStackTrace()
            throw ex
        }
    }
}