package com.thedach.kinovod.data.mapper

import com.thedach.kinovod.domain.model.Movie
import com.thedach.kinovod.domain.model.MovieRating
import com.thedach.kinovod.domain.model.Person
import com.thedach.kinovod.domain.model.Trailer
import com.thedach.network.models.GenresDto
import com.thedach.network.models.MovieDto
import com.thedach.network.models.MovieRatingDto
import com.thedach.network.models.PersonDto
import com.thedach.network.models.TrailersListDto
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class MovieMapper {


    fun mapMovieDtoToDomainModel(dto: MovieDto) = Movie(
        id = dto.id,
        name = dto.name,
        year = dto.year,
        description = dto.description,
        movieLengthMin = convertTimeOfMoviePerMin(dto.movieLength),
        movieLengthHour = convertTimeOfMoviePerHour(dto.movieLength),
        ageRating = dto.ageRating ?: NO_AGE_RATING,
        poster = dto.poster.url,
        rating = mapRating(dto.rating),
        trailers = mapTrailers(dto.trailersList),
        persons = mapPersons(dto.persons),
        genres = mapGenres(dto.genres)
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
                id = personDto.id,
                name = personDto.name ?: "no name",
                photo = personDto.photo ?: "https://st.kp.yandex.net/images/actor_iphone/iphone360_3084680.jpg"
            )
        } ?: emptyList()
    }

    private fun mapGenres(genresDto: List<GenresDto>?): List<String> {
        return genresDto?.map { genreDto ->
            genreDto.name
        } ?: emptyList()
    }

    private fun mapRating(ratingDto: MovieRatingDto): MovieRating {
        val df = DecimalFormat("0.0", DecimalFormatSymbols.getInstance(Locale.US))
        return MovieRating(
            kp = df.format(ratingDto.kp),
            imdb = df.format(ratingDto.imdb)
        )
    }

    private fun convertTimeOfMoviePerHour(movieLength: Int): Int {
        return movieLength / 60
    }
    private fun convertTimeOfMoviePerMin(movieLength: Int): Int {
        return movieLength % 60
    }

    companion object {

        private const val NO_AGE_RATING = -1
    }
}