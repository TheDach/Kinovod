package com.thedach.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiFactory {
    private const val BASE_URL = "http://8.8.8.38:8080"

    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)  // Таймаут подключения
        .readTimeout(30, TimeUnit.SECONDS)     // Таймаут чтения данных
        .writeTimeout(30, TimeUnit.SECONDS)    // Таймаут отправки данных
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .build()

    val apiService = retrofit.create(ApiService::class.java)
}