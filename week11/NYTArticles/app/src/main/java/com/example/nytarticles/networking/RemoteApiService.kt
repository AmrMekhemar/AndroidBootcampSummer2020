package com.example.nytarticles.networking
import com.example.nytarticles.model.NYTResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteApiService {
    @GET("arts.json")
     suspend fun getNYTArtsResponse(@Query("api-key") apiKey: String) : NYTResponse
}