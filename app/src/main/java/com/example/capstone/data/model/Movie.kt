package com.example.capstone.data.model

data class MovieResponse(
    val results: List<Movie>
)
data class Movie(
    val id: Int = 0,
    val title: String = "",
    val release_date: String = "",
    val vote_average: Float = 0.0f,
    val overview: String = "",
    val poster_path: String = "",
    val backdrop_path: String = ""
)

data class WatchProviderResponse(
    val results: Map<String, WatchProviderRegion>
)

data class WatchProviderRegion(
    val link: String?,
    val flatrate: List<WatchProvider>?
)

data class WatchProvider(
    val provider_name: String,
    val logo_path: String
)