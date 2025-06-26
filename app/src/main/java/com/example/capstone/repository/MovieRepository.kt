package com.example.capstone.repository

import com.example.capstone.data.api.RetrofitInstance
import com.example.capstone.data.model.Country
import com.example.capstone.data.model.Movie
import com.example.capstone.data.model.WatchProviderResponse

class MovieRepository {
    private val apiKey = ""

    suspend fun searchMovies(query: String): List<Movie> {
        val response = RetrofitInstance.api.searchMovies(apiKey, query)
        return response.results
    }
    /**
     * Fetches a list of popular movies.
     * @param region Optional region code to filter results.
     * @return A list of popular movies.
     */
    suspend fun getPopularMovies(region: String? = null): List<Movie> {
        val response = RetrofitInstance.api.getPopularMovies(apiKey, "en-US", region)
        return response.results
    }

    /**
     * Fetches a list of now playing movies.
     * @param region Optional region code to filter results.
     * @return A list of now playing movies.
     */
    suspend fun getNowPlayingMovies(region: String? = null): List<Movie> {
        val response = RetrofitInstance.api.getNowPlayingMovies(apiKey, "en-US", region)
        return response.results
    }

    /**
     * Fetches a list of upcoming movies.
     * @param region Optional region code to filter results.
     * @return A list of upcoming movies.
     */
    suspend fun getUpcomingMovies(region: String? = null): List<Movie> {
        val response = RetrofitInstance.api.getUpcomingMovies(apiKey, "en-US", region)
        return response.results
    }

    /**
     * Fetches a list of countries.
     * @return A list of countries.
     */
    suspend fun getCountries(): List<Country> {
        val response = RetrofitInstance.api.getCountries(apiKey)
        return response
    }

    /**
     * Fetches a movie by its ID.
     * @param movieId The ID of the movie to fetch.
     * @return The movie object.
     */
    suspend fun getMovieById(movieId: Int): Movie {
        return RetrofitInstance.api.getMovieById(movieId, apiKey)
    }

    /**
     * Fetches watch providers for a movie.
     * @param movieId The ID of the movie to fetch watch providers for.
     * @return The watch provider response object.
     */
    suspend fun getWatchProviders(movieId: Int): WatchProviderResponse {
        val response = RetrofitInstance.api.getWatchProviders(movieId, apiKey)
        return response
    }

}