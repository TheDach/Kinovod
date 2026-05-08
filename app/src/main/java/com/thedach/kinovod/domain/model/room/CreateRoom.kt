package com.thedach.kinovod.domain.model.room

data class CreateRoom(
    val name: String?,
    val description: String?,
    val expiresAt: String?,
    val maxMembers: Int?,
    val maxSuggestionsPerUser: Int?,
    val votingType: VotingType,
    val votingAnonymous: Boolean?,
    val votingShowResults: Boolean?,
    val votingAllowChangingVote: Boolean?,
    val useWishlists: Boolean?,
    val useWatchedLists: Boolean?,
    val genres: List<String>?,
    val yearRangeMin: Int?,
    val yearRangeMax: Int?,
    val ratingRangeMin: Int?,
    val ratingRangeMax: Int?,
    val movieType: List<Int>?,
    val countries: List<String>?,
    val durationRangeMin: Int?,
    val durationRangeMax: Int?,
    val initialMembers: List<Int>? = emptyList()
)
