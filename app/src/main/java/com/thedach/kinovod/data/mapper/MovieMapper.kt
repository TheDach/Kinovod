package com.thedach.kinovod.data.mapper

import com.thedach.kinovod.domain.model.Movie
import com.thedach.kinovod.domain.model.MovieRating
import com.thedach.kinovod.domain.model.Person
import com.thedach.kinovod.domain.model.Trailer
import com.thedach.network.models.GenresDto
import com.thedach.network.models.MovieDto
import com.thedach.network.models.PersonDto
import com.thedach.network.models.TrailersListDto

class MovieMapper {


    fun mapMovieDtoToDomainModel(dto: MovieDto) = Movie(
        id = dto.id,
        name = dto.name,
        year = dto.year,
        description = dto.description,
        movieLengthMin = convertTimeOfMoviePerMin(dto.movieLength),
        movieLengthHour = convertTimeOfMoviePerHour(dto.movieLength),
        ageRating = dto.ageRating,
        poster = dto.poster.url,
        rating = MovieRating(dto.rating.kp, dto.rating.imdb),
        trailers = mapTrailers(dto.trailersList),
        persons = mapPersons(dto.persons),
        genres = mapGenres(dto.genres),
    )

    private fun mapTrailers(trailersList: TrailersListDto?): List<Trailer> {
        return trailersList?.trailers?.map { trailerDto ->
            Trailer(
                url = trailerDto.url,
                name = trailerDto.name
            )
        } ?: emptyList()
    }

    private fun mapPersons(personsDto: List<PersonDto>?): List<Person> {
        return personsDto?.map { personDto ->
            Person(
                name = personDto.name,
                photo = personDto.photo
            )
        } ?: emptyList()
    }

    private fun mapGenres(genresDto: List<GenresDto>?): List<String>? {
        return genresDto?.map { genreDto ->
            genreDto.name
        }
    }

    private fun convertTimeOfMoviePerHour(movieLength: Int): Int {
        return movieLength / 60
    }
    private fun convertTimeOfMoviePerMin(movieLength: Int): Int {
        return movieLength % 60
    }
}