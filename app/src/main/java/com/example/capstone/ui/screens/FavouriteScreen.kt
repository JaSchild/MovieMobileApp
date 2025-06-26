package com.example.capstone.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.capstone.data.model.Movie
import com.example.capstone.ui.theme.components.MovieCard
import com.example.capstone.viewmodel.FavoriteMovieViewModel

@Composable
fun FavouriteScreen(
        navController: NavController,
        favouriteMovieViewModel: FavoriteMovieViewModel = viewModel()
) {
        val favouriteMovies by favouriteMovieViewModel.favouriteMoviesFlow.collectAsState(initial = emptyList())

        // Convert Favourite objects to Movie objects
        val movieList = favouriteMovies.map { favourite ->
                Movie(
                        id = favourite.id,
                        title = favourite.title,
                        poster_path = favourite.poster_path.toString()
                )
        }

        LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(25.dp),
                horizontalArrangement = Arrangement.spacedBy(25.dp)
        ) {
                items(movieList) { movie ->
                        MovieCard(
                                movie = movie,
                                onMovieClick = { movieId ->
                                        // Handle movie click, navigate to the movie detail screen
                                        navController.navigate("movieDetail/$movieId")
                                }
                        )
                }
        }
}