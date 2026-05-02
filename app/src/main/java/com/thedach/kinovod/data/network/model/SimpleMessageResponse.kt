package com.thedach.kinovod.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SimpleMessageResponse(
    @SerialName("message")
    val message: String?
)
