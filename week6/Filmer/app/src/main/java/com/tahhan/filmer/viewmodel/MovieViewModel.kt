package com.tahhan.filmer.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.tahhan.filmer.model.Movie
import com.tahhan.filmer.repository.MovieRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = MovieRepo(application)
    fun getMovies(): LiveData<List<Movie>> = repo.getPopularMovieList()


    fun getFavoriteMovieList(): LiveData<List<Movie>> =
        repo.getFavoriteMovieList().asLiveData()


    fun getFavoriteMovieByID(id: String): LiveData<Movie> =
        repo.getFavoriteMovieByID(id).asLiveData()


    fun insertMovie(movie: Movie) = viewModelScope.launch(Dispatchers.IO) {
        repo.insertMovie(movie)

    }

    fun removeMovie(movie: Movie) = viewModelScope.launch(Dispatchers.IO) {
        repo.removeMovie(movie)
    }

}