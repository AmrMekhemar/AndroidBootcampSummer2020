package com.example.nytarticles.networking

import com.example.nytarticles.App.Companion.BASE_URL
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.converter.gson.GsonConverterFactory


/**
 * builds retrofit and its components.
 */

fun buildApiService() : RemoteApiService = buildRetrofit().create(RemoteApiService::class.java)

fun buildRetrofit(): Retrofit {
    val contentType = "application/json".toMediaType()
    return Retrofit.Builder()
        .client(buildClient())
        .baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
}


fun buildClient(): OkHttpClient = OkHttpClient().newBuilder().build()