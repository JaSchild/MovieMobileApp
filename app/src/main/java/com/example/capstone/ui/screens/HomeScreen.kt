package com.example.capstone.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.capstone.R
import com.example.capstone.data.model.Movie
import com.example.capstone.viewmodel.MoviesViewModel
import com.example.capstone.viewmodel.SettingsViewModel

@Composable
fun HomeScreen(moviesViewModel: MoviesViewModel = viewModel(), settingsViewModel: SettingsViewModel = viewModel(), navController: NavController = rememberNavController()) {
    var query by remember { mutableStateOf(TextFieldValue("")) }
    val selectedCountry by settingsViewModel.selectedCountry.collectAsState()

    LaunchedEffect(selectedCountry) {
        moviesViewModel.getPopularMovies(selectedCountry)
        moviesViewModel.getNowPlayingMovies(selectedCountry)
        moviesViewModel.getUpcomingMovies(selectedCountry)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }

        item {
            TextField(
                value = query,
                onValueChange = { query = it },
                placeholder = {
                    Text(
                        "Search...",
                        color = Color.LightGray,
                        fontWeight = FontWeight.Bold
                    )
                },
                textStyle = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        if (query.text.isNotEmpty()) {
                            navController.navigate("search/${query.text}")
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
                    .background(
                        colorResource(id = R.color.searchbar),
                        shape = MaterialTheme.shapes.small
                    )
                    .clickable {
                        navController.navigate("search/${query.text}")
                    }
            )
        }

        item { PopularMovies(navController = navController) }
        item { NowPlayingMovies(navController = navController) }
        item { UpcomingMovies(navController = navController) }

    }
}


@Composable
fun PopularMovies(moviesViewModel: MoviesViewModel = viewModel(), navController: NavController) {
    val popularMovies by moviesViewModel.popularMovies.collectAsState()
    val loading by remember { mutableStateOf(false) }
    val error by remember { mutableStateOf("") }


    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.popular),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
        )

        when {
            loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.White)
                }
            }

            error.isNotEmpty() -> {
                Text(
                    text = "Error: $error",
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }

            else -> {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(popularMovies) { movie ->
                        MoviePosterItem(movie, navController)
                    }
                }
            }
        }
    }
}
@Composable
fun NowPlayingMovies(moviesViewModel: MoviesViewModel = viewModel(), navController: NavController) {
    val nowPlayingMovies by moviesViewModel.nowPlayingMovies.collectAsState()
    val isLoading = nowPlayingMovies.isEmpty()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.now_playing),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
        )

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth().height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        } else {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(nowPlayingMovies) { movie ->
                    MoviePosterItem(movie, navController)
                }
            }
        }
    }
}

@Composable
fun UpcomingMovies(moviesViewModel: MoviesViewModel = viewModel(), navController: NavController) {
    val upcomingMovies by moviesViewModel.upcomingMovies.collectAsState()
    val isLoading = upcomingMovies.isEmpty()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.upcoming),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
        )

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth().height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        } else {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(upcomingMovies) { movie ->
                    MoviePosterItem(movie, navController)
                }
            }
        }
        Spacer(modifier = Modifier.height(100.dp))
    }
}


@Composable
fun MoviePosterItem(movie: Movie, navController: NavController) {
    Card(
        modifier = Modifier
            .width(130.dp)
            .height(200.dp)
            .clickable {
                navController.navigate("movieDetail/${movie.id}")
            },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data("https://image.tmdb.org/t/p/w500${movie.poster_path}")
                    .crossfade(true)
                    .build()
            ),
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}
