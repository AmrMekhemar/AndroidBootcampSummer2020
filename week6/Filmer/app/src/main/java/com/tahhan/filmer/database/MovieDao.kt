package com.tahhan.filmer.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import com.tahhan.filmer.model.Movie
import kotlinx.coroutines.flow.Flow


@Dao
abstract class MovieDao {

    @Insert(onConflict = IGNORE)
    abstract suspend fun insertMovie(movie: Movie?)

    @Delete
    abstract suspend  fun deleteMovie(movie: Movie?)


    @Query("Select * from movies order by popularity Asc")
    abstract fun loadFavoriteMovies(): Flow<List<Movie>>

    @Query("SELECT * FROM movies WHERE id = :id")
    abstract  fun loadMovieById(id: String?): Flow<Movie>
}