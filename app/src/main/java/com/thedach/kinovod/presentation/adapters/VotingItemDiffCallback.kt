package com.thedach.kinovod.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import com.thedach.kinovod.domain.model.room.SuggestionWithMovie

object VotingItemDiffCallback : DiffUtil.ItemCallback<SuggestionWithMovie>() {
    override fun areItemsTheSame(
        oldItem: SuggestionWithMovie,
        newItem: SuggestionWithMovie
    ): Boolean {
        return oldItem.suggestion.suggestionId == newItem.suggestion.suggestionId
    }

    override fun areContentsTheSame(
        oldItem: SuggestionWithMovie,
        newItem: SuggestionWithMovie
    ): Boolean {
        return oldItem.suggestion == newItem.suggestion
    }
}