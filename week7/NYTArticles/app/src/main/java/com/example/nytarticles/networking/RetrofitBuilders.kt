package com.example.nytarticles.networking

import com.example.nytarticles.App.Companion.BASE_URL
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import okhttp3.MediaType.Companion.toMediaType



/**
 * builds retrofit and its components.
 */

fun buildApiService() : RemoteApiService = buildRetrofit().create(RemoteApiService::class.java)

fun buildRetrofit(): Retrofit {
    val contentType = "application/json".toMediaType()
    return Retrofit.Builder()
        .client(buildClient())
        .baseUrl(BASE_URL).addConverterFactory(Json.nonstrict.asConverterFactory(contentType)).build()
}


fun buildClient(): OkHttpClient = OkHttpClient().newBuilder().build()