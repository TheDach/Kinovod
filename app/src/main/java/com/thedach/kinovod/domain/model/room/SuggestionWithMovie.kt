package com.thedach.kinovod.domain.model.room

import com.thedach.kinovod.domain.model.movie.Movie

data class SuggestionWithMovie(
    val suggestion: RoomSuggestion,
    val movie: Movie,
    val progressPercent: Int,
    val votingAnonymous: Boolean,
    val votingType: VotingType
)
