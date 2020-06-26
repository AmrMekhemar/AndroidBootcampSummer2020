package com.tahhan.filmer.networking

import com.tahhan.filmer.Constants
import com.tahhan.filmer.model.MovieResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface MyWebService {
    companion object{
        val retrofit: Retrofit
            get() = Retrofit.Builder()
                .baseUrl(Constants.BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }


    @GET("movie/popular")
    fun discoverPopularMovie(@Query("api_key") apiKey: String?): Call<MovieResponse?>?

}