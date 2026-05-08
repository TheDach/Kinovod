package com.thedach.kinovod.data.mapper

import com.thedach.kinovod.data.network.model.room.response.MatchResponseDto
import com.thedach.kinovod.data.network.model.room.response.MemberResponseDto
import com.thedach.kinovod.data.network.model.room.response.RoomDetailsResponseDto
import com.thedach.kinovod.data.network.model.room.response.RoomResponseDto
import com.thedach.kinovod.data.network.model.room.response.SuggestionResponseDto
import com.thedach.kinovod.data.network.model.room.response.UserRoomsResponseDto
import com.thedach.kinovod.domain.model.room.MemberRole
import com.thedach.kinovod.domain.model.room.Room
import com.thedach.kinovod.domain.model.room.RoomMatch
import com.thedach.kinovod.domain.model.room.RoomMember
import com.thedach.kinovod.domain.model.room.RoomSuggestion
import com.thedach.kinovod.domain.model.room.VotingType

class RoomMapper {

    fun mapUserRoomsDtoToDomain(
        userRoomsDto: UserRoomsResponseDto
    ) : List<Room> {
        return userRoomsDto.rooms?.map { roomDto ->
            mapRoomDtoToDomain(roomDto)
        } ?: emptyList()
    }

    fun mapRoomDetailsDtoToDomain(roomDetailsDto: RoomDetailsResponseDto) : Room? {
        return roomDetailsDto.room?.let { mapRoomDtoToDomain(it) }
    }

    fun mapRoomDtoToDomain(roomDto: RoomResponseDto): Room {
        return Room(
            roomId = roomDto.roomId,
            name = roomDto.name,
            description = roomDto.description,
            createdAt = roomDto.createdAt,
            expiresAt = roomDto.expiresAt,
            maxMembers = roomDto.maxMembers,
            maxSuggestionsPerUser = roomDto.maxSuggestionsPerUser,
            votingType = VotingType.fromValue(roomDto.votingType),
            votingAnonymous = roomDto.votingAnonymous,
            votingShowResults = roomDto.votingShowResults,
            votingAllowChangingVote = roomDto.votingAllowChangingVote,
            useWishlists = roomDto.useWishlists,
            useWatchedLists = roomDto.useWatchedLists,
            genres = roomDto.genres,
            yearRangeMin = roomDto.yearRangeMin,
            yearRangeMax = roomDto.yearRangeMax,
            ratingRangeMin = roomDto.ratingRangeMin,
            ratingRangeMax = roomDto.ratingRangeMax,
            movieType = roomDto.movieType,
            countries = roomDto.countries,
            durationRangeMin = roomDto.durationRangeMin,
            durationRangeMax = roomDto.durationRangeMax,
            isAdmin = roomDto.isAdmin,
            members = mapMembersDtoToDomain(roomDto.members),
            suggestions = mapSuggestionsDtoToDomain(roomDto.suggestions),
            matches = mapMatchToDomain(roomDto.matches)
        )
    }

    fun mapMembersDtoToDomain(members: List<MemberResponseDto>):List<RoomMember> {
        return members.map { memberDto ->
            RoomMember(
                userId = memberDto.userId,
                username = memberDto.username,
                userTag = memberDto.userTag,
                avatar = memberDto.avatar,
                role = MemberRole.fromValue(memberDto.role)
            )
        }
    }

    fun mapSuggestionsDtoToDomain(suggestions: List<SuggestionResponseDto>): List<RoomSuggestion> {
        return suggestions.map { suggestionDto ->
            RoomSuggestion(
                suggestionId = suggestionDto.suggestionId,
                movieId = suggestionDto.movieId,
                suggestedBy = suggestionDto.suggestedBy,
                suggestedByUsername = suggestionDto.suggestedByUsername,
                voters = suggestionDto.voters
            )
        }
    }

    fun mapMatchToDomain(matches: Map<Int, MatchResponseDto>): Map<Int, RoomMatch> {
        return matches.mapValues { (_, matchDto) ->
            RoomMatch(
                voters = matchDto.voters,
                count = matchDto.count
            )
        }
    }
}