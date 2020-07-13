package com.example.nytarticles.networking
import com.example.nytarticles.model.NYTResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteApiService {
    @GET("arts.json")
     suspend fun getNYTArtsResponse(@Query("api-key") apiKey: String?) : NYTResponse

    @GET("home.json")
    suspend fun getNYTHomeResponse(@Query("api-key") apiKey: String?) : NYTResponse

    @GET("science.json")
    suspend fun getNYTScienceResponse(@Query("api-key") apiKey: String?) : NYTResponse

    @GET("world.json")
    suspend fun getNYTWorldResponse(@Query("api-key") apiKey: String?) : NYTResponse
}