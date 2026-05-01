package com.thedach.network.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MovieDto(

    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("year")
    @Expose
    val year: Int,
    @SerializedName("description")
    @Expose
    val description: String,
    @SerializedName("movieLength")
    @Expose
    val movieLength: Int,
    @SerializedName("ageRating")
    @Expose
    val ageRating: Int?,


    @SerializedName("rating")
    @Expose
    val rating: MovieRatingDto,
    @SerializedName("videos")
    @Expose
    val trailersList: TrailersListDto?,
    @SerializedName("poster")
    @Expose
    val poster: PosterDto,
    @SerializedName("persons")
    @Expose
    val persons: List<PersonDto>,
    @SerializedName("genres")
    @Expose
    val genres: List<GenresDto>
)
