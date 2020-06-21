package com.tahhan.filmer.database

import android.content.Context
import androidx.room.RoomDatabase
import com.tahhan.filmer.model.MovieResponse
import androidx.room.Database;
import androidx.room.Room

import com.tahhan.filmer.model.Movie;

@Database(entities = [Movie::class], version = 1, exportSchema = false)
abstract class MovieDB : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: MovieDB? = null
        fun getDatabase(context: Context): MovieDB {
            val tempInstance = INSTANCE
            if (tempInstance != null) return tempInstance

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDB::class.java,
                    "word_database"
                ).allowMainThreadQueries().build()
                INSTANCE = instance
                return instance
            }

        }
    }
}
