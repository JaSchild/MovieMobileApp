package com.example.capstone.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstone.data.model.PlaceResult
import com.example.capstone.repository.PlacesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlacesViewModel : ViewModel() {

    private val repository = PlacesRepository()
    
    private val _cinemas = MutableStateFlow<List<PlaceResult>>(emptyList())
    val cinemas: StateFlow<List<PlaceResult>> = _cinemas

    private val _error = MutableStateFlow<String?>(null)

    /**
     * Fetches nearby cinemas based on the provided location and radius.
     * @param location The location to search for nearby cinemas.
     * @param radius The radius in meters to search for cinemas.
     */
    fun fetchNearbyCinemas(location: String, radius: Int) {
        viewModelScope.launch {
            try {
                val results = repository.getNearbyCinemas(location, radius)
                _cinemas.value = results
                println("Cinemas fetched: ${results.size} results")
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to load cinemas: ${e.message}"
            }
        }
    }
}
