package com.thedach.kinovod.data.network.model.room.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoomResponseDto(
    @SerialName("roomId")
    val roomId: Int,

    @SerialName("name")
    val name: String?,

    @SerialName("description")
    val description: String?,

    @SerialName("createAt")
    val createdAt: String?,

    @SerialName("expiresAt")
    val expiresAt: String?,

    @SerialName("maxMemvers")
    val maxMembers: Int?,

    @SerialName("MaxSuggestionsPerUser")
    val maxSuggestionsPerUser: Int?,

    @SerialName("votingType")
    val votingType: Int,

    @SerialName("votingAnonymous")
    val votingAnonymous: Boolean?,

    @SerialName("votingShowResults")
    val votingShowResults: Boolean?,

    @SerialName("votingAllowChangingVote")
    val votingAllowChangingVote: Boolean?,

    @SerialName("useWishlists")
    val useWishlists: Boolean?,

    @SerialName("useWatchedLists")
    val useWatchedLists: Boolean?,

    @SerialName("genres")
    val genres: List<String>?,

    @SerialName("yearRangeMin")
    val yearRangeMin: Int?,

    @SerialName("yearRangeMax")
    val yearRangeMax: Int?,

    @SerialName("ratingRangeMin")
    val ratingRangeMin: Int?,

    @SerialName("ratingRangeMax")
    val ratingRangeMax: Int?,

    @SerialName("movieType")
    val movieType: List<Int>?,

    @SerialName("countries")
    val countries: List<String>?,

    @SerialName("durationRangeMin")
    val durationRangeMin: Int?,

    @SerialName("durationRangeMax")
    val durationRangeMax: Int?,

    @SerialName("isAdmin")
    val isAdmin: Boolean,

    @SerialName("members")
    val members: List<MemberResponseDto>,

    @SerialName("suggestions")
    val suggestions: List<SuggestionResponseDto>,

    @SerialName("matches")
    val matches: Map<Int, MatchResponseDto>
)
