package com.example.capstone.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.capstone.viewmodel.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.capstone.ui.theme.util.showLeavingSoonNotification
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InfoScreen(
    movieId: Int,
    navController: NavController,
    moviesViewModel: MoviesViewModel = viewModel(),
    settingsViewModel: SettingsViewModel = viewModel(),
    favoriteMovieViewModel: FavoriteMovieViewModel = viewModel(),
    placesViewModel: PlacesViewModel = viewModel()
) {
    val context = LocalContext.current

    val movie by moviesViewModel.selectedMovie.collectAsState()
    val providers by moviesViewModel.watchProviders.collectAsState()
    val region by settingsViewModel.selectedCountry.collectAsState()
    val isFavourite by favoriteMovieViewModel
        .isMovieFavourite(movieId)
        .collectAsState(initial = false)

    val rawCinemas by placesViewModel.cinemas.collectAsState(initial = emptyList())

    val fallbackLocation = LatLng(52.5692214, 4.9934757 ) // Oosthuizen

    val nearbyCinemas = remember(rawCinemas) {
        rawCinemas.sortedBy {
            val lat = it.geometry.location.lat
            val lng = it.geometry.location.lng
            val result = FloatArray(1)
            Location.distanceBetween(
                fallbackLocation.latitude, fallbackLocation.longitude,
                lat, lng, result
            )
            result[0]
        }
    }

    val subscriptionPrices = mapOf(
        "Netflix" to "$6.99/mo",
        "Hulu" to "$7.99/mo",
        "Disney Plus" to "$7.99/mo",
        "HBO Max" to "$15.99/mo",
        "Amazon Prime Video" to "$8.99/mo",
        "Paramount Plus" to "$5.99/mo",
        "Apple TV Plus" to "$6.99/mo"
    )

    LaunchedEffect(movieId, region) {
        if (region.isNotBlank()) {
            moviesViewModel.fetchMovieDetail(movieId, region)
        }
    }

    LaunchedEffect(Unit) {
        val locationStr = "${fallbackLocation.latitude},${fallbackLocation.longitude}"
        placesViewModel.fetchNearbyCinemas(locationStr, 20000)
    }
    LaunchedEffect(movie) {
        movie?.let {
            val leavingDate = LocalDate.of(2025, 4, 15)
            val today = LocalDate.now()
            val daysLeft = ChronoUnit.DAYS.between(today, leavingDate)

            if (daysLeft in 1..6) {
                showLeavingSoonNotification(context, it.title, daysLeft)
            }
        }
    }
    if (movie != null) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Go Back",
                    tint = colorResource(id = com.example.capstone.R.color.white)
                )
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w500${movie!!.poster_path}",
                    contentDescription = movie!!.title,
                    modifier = Modifier
                        .width(120.dp)
                        .height(180.dp)
                        .padding(end = 16.dp)
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = movie!!.title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = colorResource(id = com.example.capstone.R.color.white)
                    )
                    Text(
                        text = "Rating: ${movie!!.vote_average}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorResource(id = com.example.capstone.R.color.white)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            IconButton(onClick = {
                movie?.let {
                    favoriteMovieViewModel.toggleFavourite(it)
                }
            }) {
                Icon(
                    imageVector = if (isFavourite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Toggle Favorite",
                    tint = colorResource(id = com.example.capstone.R.color.white)
                )
            }

            Text(text = movie!!.overview, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Available on:", style = MaterialTheme.typography.titleMedium)
            if (providers.isNotEmpty()) {
                providers.forEach { provider ->
                    val price = subscriptionPrices[provider.provider_name] ?: "Price not available"

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        AsyncImage(
                            model = "https://image.tmdb.org/t/p/w92${provider.logo_path}",
                            contentDescription = provider.provider_name,
                            modifier = Modifier
                                .size(50.dp)
                                .padding(end = 12.dp)
                        )

                        Column {
                            Text(
                                text = provider.provider_name,
                                style = MaterialTheme.typography.bodyLarge,
                                color = colorResource(id = com.example.capstone.R.color.white)
                            )
                            Text(
                                text = price,
                                style = MaterialTheme.typography.bodySmall,
                                color = colorResource(id = com.example.capstone.R.color.white)
                            )
                        }
                    }
                }
            } else {
                Text("No streaming options found for region: $region")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Nearby Cinemas", style = MaterialTheme.typography.titleMedium)

            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(fallbackLocation, 12f)
            }

            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                cameraPositionState = cameraPositionState
            ) {
                Marker(state = MarkerState(fallbackLocation), title = "You are here")

                nearbyCinemas.forEach { place ->
                    val latLng = LatLng(place.geometry.location.lat, place.geometry.location.lng)
                    Marker(state = MarkerState(latLng), title = place.name)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            nearbyCinemas.forEach { cinema ->
                CardItem(cinema.name)
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    } else {
        Text("Loading...", modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun CardItem(name: String) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(40.dp),
        shape = RoundedCornerShape(13.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = com.example.capstone.R.color.black)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(10.dp)
        )
    }
}
