package com.thedach.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiFactory {
    private const val BASE_URL = "http://8.8.8.38:8080"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    val apiService = retrofit.create(ApiService::class.java)
}