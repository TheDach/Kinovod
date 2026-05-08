package com.thedach.kinovod.domain.model.room

enum class MemberRole(
    val value: Int
) {
    CREATOR(0),
    MEMBER(1);

    companion object {
        fun fromValue(value: Int): MemberRole {
            return MemberRole.entries.find { it.value == value } ?: MEMBER
        }
    }
}