package com.example.capstone.database

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("settings")

class CountryPreferenceManager(private val context: Context) {
    companion object {
        val COUNTRY_KEY = stringPreferencesKey("selected_country")
    }

    val selectedCountry: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[COUNTRY_KEY]
        }

    suspend fun saveCountry(code: String) {
        context.dataStore.edit { preferences ->
            preferences[COUNTRY_KEY] = code
        }
    }
}
