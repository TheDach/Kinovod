package com.thedach.kinovod.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import com.thedach.kinovod.domain.model.movie.Review

object ReviewItemDiffCallback: DiffUtil.ItemCallback<Review>() {
    override fun areItemsTheSame(
        oldItem: Review,
        newItem: Review
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Review,
        newItem: Review
    ): Boolean {
        return oldItem == newItem
    }
}