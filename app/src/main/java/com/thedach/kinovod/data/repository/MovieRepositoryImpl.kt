package com.thedach.kinovod.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.thedach.kinovod.data.mapper.MovieMapper
import com.thedach.kinovod.domain.MovieRepository
import com.thedach.kinovod.domain.model.Movie
import com.thedach.network.ApiFactory

class MovieRepositoryImpl: MovieRepository {

    private val apiService = ApiFactory.apiService
    private val mapper = MovieMapper()

    override suspend fun getMovieList(
        selectFields: String?,
        ratingKp: String?,
        genreName: String?
    ): List<Movie> {

        return try {
            val response = apiService.getMovies(
                selectFields = selectFields,
                ratingKp = ratingKp,
                genreName = genreName
            )

            response.movies?.map{ mapper.mapMovieDtoToDomainModel(it) } ?: emptyList()

        } catch (ex: Exception) {
            ex.printStackTrace()
            throw ex
        }
    }
}