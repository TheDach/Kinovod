package com.thedach.kinovod.domain.model.movie

enum class SearchMode {
    ALL_MOVIES,         // Поиск без нешних фильтров
    ROOM_SUGGESTION;    // Поиск по фильтрам выбранным в комнате
}