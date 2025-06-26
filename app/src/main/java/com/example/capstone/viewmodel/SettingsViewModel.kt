package com.example.capstone.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstone.data.model.Country
import com.example.capstone.database.CountryPreferenceManager
import com.example.capstone.repository.MovieRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = MovieRepository()
    private val preferenceManager = CountryPreferenceManager(application)

    private val _countries = MutableStateFlow<List<Country>>(emptyList())
    val countries: StateFlow<List<Country>> = _countries

    private val _selectedCountry = MutableStateFlow<String>("Select Country")
    val selectedCountry: StateFlow<String> = _selectedCountry


    init {
       // Initialize the ViewModel by fetching the list of countries and observing the saved country code
        getCountries()
        observeSavedCountry()
    }

    /**
     * Fetches the list of countries from the repository and updates the countries state.
     */
    fun getCountries() {
        viewModelScope.launch {
            try {
                _countries.value = repository.getCountries()
            } catch (e: Exception) {
                _countries.value = emptyList()
            }
        }
    }

    /**
     * Observes the saved country code from preferences and updates the selected country state.
     */
    private fun observeSavedCountry() {
        viewModelScope.launch {
            preferenceManager.selectedCountry.collect { savedCode ->
                savedCode?.let {
                    _selectedCountry.value = it
                }
            }
        }
    }

    /**
     * Sets the selected country code and saves it to preferences.
     * @param code The country code to set as selected.
     */
    fun setSelectedCountry(code: String) {
        viewModelScope.launch {
            preferenceManager.saveCountry(code)
        }
        _selectedCountry.value = code
    }
}
