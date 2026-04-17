package com.thedach.kinovod.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.thedach.kinovod.R
import com.thedach.kinovod.databinding.ItemReviewBinding
import com.thedach.kinovod.domain.model.Review

class ReviewAdapter(
    private val context: Context
): ListAdapter<Review, ReviewViewHolder>(ReviewItemDiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReviewViewHolder {
        val binding = ItemReviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ReviewViewHolder,
        position: Int
    ) {
        val review = getItem(position)

        with(holder.binding){

            tvReviewAuthor.text = review.author
            tvReviewRating.text = review.typeRating
            tvReviewText.text = review.review

            val colorId = when(review.typeRating) {
                TYPE_POSITIVE -> context.resources.getColor(
                    R.color.bg_review_positive,
                    null
                )
                TYPE_NEUTRAL -> context.resources.getColor(
                    R.color.bg_review_neutral,
                    null
                )
                else -> context.resources.getColor(
                    R.color.bg_review_negative,
                    null
                )
            }

            linearLayoutReview.setBackgroundColor(colorId)

        }
    }

    companion object {
        private val TYPE_POSITIVE: String = "Позитивный"
        private val TYPE_NEGATIVE: String = "Негативный"
        private val TYPE_NEUTRAL: String = "Нейтральный"
    }
}