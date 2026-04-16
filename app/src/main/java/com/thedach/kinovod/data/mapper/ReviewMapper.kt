package com.thedach.kinovod.data.mapper

import com.thedach.kinovod.data.network.model.ReviewDto
import com.thedach.kinovod.domain.model.Review

class ReviewMapper {

    fun mapReviewDtoToDomainModel(dto: ReviewDto) = Review(
        id = dto.id,
        review = dto.review,
        typeRating = dto.typeRating,
        author = dto.author
    )
}