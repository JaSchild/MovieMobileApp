package com.example.capstone.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.capstone.data.model.Favourite
import com.example.capstone.data.model.Movie
import com.example.capstone.repository.FavouriteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoriteMovieViewModel(application: Application) : AndroidViewModel(application) {
    private val favouriteRepository = FavouriteRepository(getApplication())
    private val mainScope = CoroutineScope(Dispatchers.Main)

    val favouriteMoviesFlow = favouriteRepository.getFavourites()
        .stateIn(mainScope, SharingStarted.Lazily, emptyList())


    /**
     * Toggles the favourite status of a movie.
     * If the movie is already a favourite, it will be removed from favourites.
     * If the movie is not a favourite, it will be added to favourites.
     *
     * @param movie The movie to toggle as favourite.
     */
    fun toggleFavourite(movie: Movie) {
        mainScope.launch(Dispatchers.IO) {
            val isFav = favouriteRepository.isFavourite(movie.id).first()
            val fav = Favourite(
                id = movie.id,
                title = movie.title,
                poster_path = movie.poster_path,
                vote_average = movie.vote_average,
                overview = movie.overview
            )
            if (isFav) {
                favouriteRepository.delete(fav)
            } else {
                favouriteRepository.insert(fav)
            }
        }
    }

    /**
     * Checks if a movie is marked as favourite.
     *
     * @param movieId The ID of the movie to check.
     * @return A Flow that emits true if the movie is a favourite, false otherwise.
     */
    fun isMovieFavourite(movieId: Int): Flow<Boolean> {
        return favouriteRepository.isFavourite(movieId)
    }
}
