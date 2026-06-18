package com.thedach.kinovod.domain.model.movie

enum class SearchMode {
    ALL_MOVIES,         // Поиск без внешних фильтров
    ROOM_SUGGESTION,    // Поиск по фильтрам выбранным в комнате
    PROFILE_MOVIES;     // Для отображения списка фильмов "Понравившиеся" и "Просмотренные"
}