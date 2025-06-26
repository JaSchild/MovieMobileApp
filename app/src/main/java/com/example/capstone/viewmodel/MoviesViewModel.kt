package com.example.capstone.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstone.data.model.Movie
import com.example.capstone.data.model.WatchProvider
import com.example.capstone.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.concurrent.TimeoutException

class MoviesViewModel : ViewModel() {
    private val repository = MovieRepository()

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> get() = _movies

    private val _popularMovies = MutableStateFlow<List<Movie>>(emptyList())
    val popularMovies: StateFlow<List<Movie>> get() = _popularMovies

    private val _nowPlayingMovies = MutableStateFlow<List<Movie>>(emptyList())
    val nowPlayingMovies: StateFlow<List<Movie>> get() = _nowPlayingMovies

    private val _upcomingMovies = MutableStateFlow<List<Movie>>(emptyList())
    val upcomingMovies: StateFlow<List<Movie>> get() = _upcomingMovies

    private val _selectedMovie = MutableStateFlow<Movie?>(null)
    val selectedMovie: StateFlow<Movie?> get() = _selectedMovie

    private val _watchProviders = MutableStateFlow<List<WatchProvider>>(emptyList())
    val watchProviders: StateFlow<List<WatchProvider>> get() = _watchProviders

    private val _loading = MutableStateFlow(false)

    private val _error = MutableStateFlow<String?>(null)

    /**
     * Searches for movies based on the provided query.
     */
    fun searchMovies(query: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                val result = repository.searchMovies(query)
                _movies.value = result
            } catch (e: IOException) {
                _error.value = "Network error: ${e.message}. Please check your internet connection."
            } catch (e: TimeoutException) {
                _error.value = "Request timed out. Please try again later."
            } catch (e: Exception) {
                _error.value = "Something went wrong: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    /**
     * Fetches popular movies based on the provided region.
     */
    fun getPopularMovies(region: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                val result = repository.getPopularMovies(region)
                _popularMovies.value = result
            } catch (e: IOException) {
                _error.value = "Network error: ${e.message}. Please check your internet connection."
            } catch (e: TimeoutException) {
                _error.value = "Request timed out. Please try again later."
            } catch (e: Exception) {
                _error.value = "Something went wrong: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    /**
     * Fetches now playing movies based on the provided region.
     */
    fun getNowPlayingMovies(region: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                val result = repository.getNowPlayingMovies(region)
                _nowPlayingMovies.value = result
                println("Now playing movies: $result")
            } catch (e: Exception) {
                _error.value = "Failed to fetch movies: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    /**
     * Fetches upcoming movies based on the provided region.
     */
    fun getUpcomingMovies(region: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                val result = repository.getUpcomingMovies(region)
                _upcomingMovies.value = result
            } catch (e: IOException) {
                _error.value = "Network error: ${e.message}. Please check your internet connection."
            } catch (e: TimeoutException) {
                _error.value = "Request timed out. Please try again later."
            } catch (e: Exception) {
                _error.value = "Something went wrong: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }



    /**
     * Fetches movie details and watch providers based on the provided movie ID and region.
     */
    fun fetchMovieDetail(movieId: Int, region: String = "US") {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                val movie = repository.getMovieById(movieId)
                _selectedMovie.value = movie

                val allProviders = repository.getWatchProviders(movieId)
                val countryProviders = allProviders.results[region]?.flatrate
                if (countryProviders != null) {
                    _watchProviders.value = countryProviders
                }
            } catch (e: IOException) {
                _error.value = "Network error: ${e.message}. Please check your internet connection."
            } catch (e: TimeoutException) {
                _error.value = "Request timed out. Please try again later."
            } catch (e: Exception) {
                _error.value = "Something went wrong: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

}
