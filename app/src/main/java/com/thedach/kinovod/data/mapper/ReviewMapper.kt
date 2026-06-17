package com.thedach.kinovod.data.mapper

import com.thedach.kinovod.data.network.model.movie.ReviewDto
import com.thedach.kinovod.domain.model.movie.Review
import javax.inject.Inject

class ReviewMapper @Inject constructor() {

    fun mapReviewDtoToDomainModel(dto: ReviewDto) = Review(
        id = dto.id,
        review = dto.review,
        typeRating = dto.typeRating,
        author = dto.author
    )
}