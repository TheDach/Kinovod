package com.thedach.kinovod.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import com.thedach.kinovod.domain.model.Movie

object MovieItemDiffCallback: DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(
        oldItem: Movie,
        newItem: Movie
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Movie,
        newItem: Movie
    ): Boolean {
        return oldItem == newItem
    }
}