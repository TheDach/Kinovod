package com.thedach.kinovod.domain.model.room

enum class VotingType(
    val value: Int
) {
    SINGLE(0),      // Один голос
    MULTIPLE(1),    // Множественные голоса
    PRIORITY(2);    // Приоритетное голосование

    companion object {
        fun fromValue(value: Int): VotingType {
            return VotingType.entries.find { it.value == value } ?: SINGLE
        }
    }
}