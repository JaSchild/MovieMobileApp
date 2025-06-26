package com.example.capstone.data.api

import com.example.capstone.data.model.PlacesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApi {
    @GET("nearbysearch/json")
    suspend fun getNearbyCinemas(
        @Query("key") apiKey: String,
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("type") type: String = "movie_theater"
    ): PlacesResponse
}