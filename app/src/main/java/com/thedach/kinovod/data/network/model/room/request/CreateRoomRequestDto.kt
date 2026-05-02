package com.thedach.kinovod.data.network.model.room.request

data class CreateRoomRequestDto(
    val name: String?,
    val description: String?,
    val expiresAt: String?,
    val maxMembers: Int?,
    val maxSuggestionsPerUser: Int?,
    val votingType: Int, // 0 - single, 1 - multiple, 2 - priority
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
