package com.thedach.kinovod.domain.model.movie

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Settings(
    val genres: List<String>?,

    /* В идеяле добавить еще настройки дя фильмров поиска фильмов */
    /* Жанры, тип кино, года, рейтинг и т.д. */

): Parcelable
