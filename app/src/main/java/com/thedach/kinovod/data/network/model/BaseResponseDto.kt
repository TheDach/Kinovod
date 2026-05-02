package com.thedach.kinovod.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseResponseDto<T>(

    @SerialName("message")
    val message: String?,
    val data: T?
)
