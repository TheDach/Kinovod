package com.thedach.kinovod.domain.model.room

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Room(
    val roomId: Int,
    val name: String?,
    val description: String?,
    val createdAt: String?,
    val expiresAt: String?,
    val maxMembers: Int,
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
    val isAdmin: Boolean,
    val members: List<RoomMember>,
    val suggestions: List<RoomSuggestion>,
    val matches: Map<Int, RoomMatch>
) : Parcelable
