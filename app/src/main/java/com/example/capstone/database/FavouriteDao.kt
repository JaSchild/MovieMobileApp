package com.example.capstone.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.capstone.data.model.Favourite
import kotlinx.coroutines.flow.Flow


@Dao
interface FavouriteDao {
    @Query("SELECT * FROM favourite_movies")
    fun getFavourites(): Flow<List<Favourite>>

    @Insert
    suspend fun insert(favourite: Favourite): Long

    @Delete
    suspend fun delete(movie: Favourite)

    @Query("SELECT EXISTS(SELECT * FROM favourite_movies WHERE id = :movieId)")
    fun isFavourite(movieId: Int): Flow<Boolean>
}