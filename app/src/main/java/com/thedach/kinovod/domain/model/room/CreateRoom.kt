package com.thedach.kinovod.domain.model.room

data class CreateRoom(
    val name: String? = null,
    val description: String? = null,
    val expiresAt: String? = null,
    val maxMembers: Int? = null,
    val maxSuggestionsPerUser: Int? = null,
    val votingType: VotingType,
    val votingAnonymous: Boolean? = null,
    val votingShowResults: Boolean? = null,
    val votingAllowChangingVote: Boolean? = null,
    val useWishlists: Boolean? = null,
    val useWatchedLists: Boolean? = null,
    val genres: List<String>? = emptyList(),
    val yearRangeMin: Int? = null,
    val yearRangeMax: Int? = null,
    val ratingRangeMin: Int? = null,
    val ratingRangeMax: Int? = null,
    val movieType: List<Int>? = emptyList(),
    val countries: List<String>? = emptyList(),
    val durationRangeMin: Int? = null,
    val durationRangeMax: Int? = null,
    val initialMembers: List<Int>? = emptyList()
)
