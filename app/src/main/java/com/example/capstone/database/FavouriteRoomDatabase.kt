package com.example.capstone.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.TypeConverters
import com.example.capstone.data.model.Favourite

@Database(entities = [Favourite::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class FavouriteRoomDatabase : RoomDatabase() {

    abstract fun favouriteDao(): FavouriteDao

    companion object {
        private const val DATABASE_NAME = "FAVOURITE_DATABASE"

        @Volatile
        private var INSTANCE: FavouriteRoomDatabase? = null

        fun getDatabase(context: Context): FavouriteRoomDatabase? {
            if (INSTANCE == null) {
                synchronized(FavouriteRoomDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            FavouriteRoomDatabase::class.java, DATABASE_NAME
                        )
                            .allowMainThreadQueries()
                            .build()

                    }
                }
            }
            return INSTANCE
        }
    }
}