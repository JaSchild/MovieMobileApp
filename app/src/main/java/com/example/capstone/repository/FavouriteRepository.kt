package com.example.capstone.repository

import android.content.Context
import com.example.capstone.data.model.Favourite
import com.example.capstone.database.FavouriteDao
import com.example.capstone.database.FavouriteRoomDatabase

class FavouriteRepository(context: Context) {
    private val favouriteDao: FavouriteDao

    init {
        val database = FavouriteRoomDatabase.getDatabase(context)
        favouriteDao = database!!.favouriteDao()
    }

    /**
     * Inserts a favourite movie into the database.
     * @param favourite The favourite movie to insert.
     */
    suspend fun insert(favourite: Favourite) = favouriteDao.insert(favourite)

    /**
     * Deletes a favourite movie from the database.
     * @param favourite The favourite movie to delete.
     */
    suspend fun delete(favourite: Favourite) = favouriteDao.delete(favourite)

    /**
     * Retrieves all favourite movies from the database.
     * @return A list of favourite movies.
     */
    fun getFavourites() = favouriteDao.getFavourites()

    /**
     * Checks if a movie is a favourite.
     * @param movieId The ID of the movie to check.
     * @return True if the movie is a favourite, false otherwise.
     */
    fun isFavourite(movieId: Int) = favouriteDao.isFavourite(movieId)
}