package com.example.capstone.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.capstone.viewmodel.MoviesViewModel
import androidx.compose.ui.text.input.ImeAction
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.capstone.R
import com.example.capstone.ui.theme.components.MovieCard

@Composable
fun SearchScreen(
    navController: NavController,
    navBackStackEntry: NavBackStackEntry,
    moviesViewModel: MoviesViewModel = viewModel()
) {
    val query = navBackStackEntry.arguments?.getString("query") ?: ""
    var textFieldValue by remember { mutableStateOf(TextFieldValue(query)) }

    LaunchedEffect(query) {
        if (query.isNotEmpty()) {
            moviesViewModel.searchMovies(query)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = textFieldValue,
            onValueChange = { textFieldValue = it },
            placeholder = { Text(stringResource(id = R.string.search_placeholder), color = Color.LightGray, fontWeight = FontWeight.Bold) },
            textStyle = TextStyle(color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    if (textFieldValue.text.isNotEmpty()) {
                        moviesViewModel.searchMovies(textFieldValue.text)
                    }
                }
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colorResource(id = R.color.searchbar),
                unfocusedContainerColor = colorResource(id = R.color.searchbar),
                focusedTextColor = colorResource(id = R.color.searchbar_text),
                unfocusedTextColor = colorResource(id = R.color.searchbar_text),
                focusedPlaceholderColor = colorResource(id = R.color.searchbar_text),
                unfocusedPlaceholderColor = colorResource(id = R.color.searchbar_text),
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(colorResource(id = R.color.searchbar), shape = MaterialTheme.shapes.small)
        )

        Spacer(modifier = Modifier.height(16.dp))

        val movies by moviesViewModel.movies.collectAsState(initial = emptyList())

        Box(modifier = Modifier.fillMaxSize()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(25.dp),
                horizontalArrangement = Arrangement.spacedBy(25.dp)
            ) {
                items(movies.size) { index ->
                    val movie = movies[index]
                    MovieCard(
                        movie = movie,
                        onMovieClick = { movieId ->
                            // Handle movie click, navigate to the movie detail screen
                            navController.navigate("movieDetail/$movieId")
                        }
                    )
                }
            }

            // Show the "No Movies Found" text if movies are empty
            if (movies.isEmpty()) {
                Text(
                    text = stringResource(id = R.string.no_movies_found),
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .align(Alignment.Center) // Ensure it is centered
                        .padding(16.dp) // Add padding to avoid overlap with the grid
                )
            }
        }
    }
}
