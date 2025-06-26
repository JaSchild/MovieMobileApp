package com.example.capstone.ui.screens

import android.app.Application
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.capstone.R
import com.example.capstone.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val settingsViewModel: SettingsViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SettingsViewModel(context.applicationContext as Application) as T
            }
        }
    )

    val countries by settingsViewModel.countries.collectAsState(initial = emptyList())
    val selectedCountry by settingsViewModel.selectedCountry.collectAsState()

    var expanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    val filteredCountries = countries.filter {
        it.english_name.contains(searchQuery.text, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp),
            color = colorResource(id = R.color.white),
            fontWeight = FontWeight.Bold,
        )

        Button(
            onClick = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = selectedCountry)
        }

        if (expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search countries") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = colorResource(id = R.color.searchbar),
                        unfocusedContainerColor = colorResource(id = R.color.searchbar),
                        focusedTextColor = colorResource(id = R.color.searchbar_text),
                        unfocusedTextColor = colorResource(id = R.color.searchbar_text),
                        focusedPlaceholderColor = colorResource(id = R.color.searchbar_text),
                        unfocusedPlaceholderColor = colorResource(id = R.color.searchbar_text),
                    ),
                )

                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(filteredCountries.size) { index ->
                        val country = filteredCountries[index]
                        Button(
                            onClick = {
                                settingsViewModel.setSelectedCountry(country.iso_3166_1)
                                expanded = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = country.english_name)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "You selected: $selectedCountry",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
