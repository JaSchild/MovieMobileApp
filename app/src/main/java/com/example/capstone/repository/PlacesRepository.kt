package com.example.capstone.repository

import com.example.capstone.data.api.PlacesRetrofitInstance
import com.example.capstone.data.model.PlaceResult

class PlacesRepository {
    private val apiKey = ""

    /**
     * Fetches nearby cinemas based on the provided location and radius.
     *
     * @param location The location to search for nearby cinemas.
     * @param radius The radius in meters to search for cinemas.
     * @return A list of PlaceResult objects representing the nearby cinemas.
     */
    suspend fun getNearbyCinemas(
        location: String,
        radius: Int
    ): List<PlaceResult> {
        val response = PlacesRetrofitInstance.api.getNearbyCinemas(
            apiKey = apiKey,
            location = location,
            radius = radius

        )
        return response.results
    }
}
