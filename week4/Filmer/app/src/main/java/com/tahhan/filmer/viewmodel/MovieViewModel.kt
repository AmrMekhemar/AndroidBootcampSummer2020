package com.tahhan.filmer.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tahhan.filmer.model.Movie
import com.tahhan.filmer.repository.MovieRepo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MovieViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = MovieRepo(application)
    private  var rvPosition: MutableLiveData<Int> = MutableLiveData()
    fun getMovies(): LiveData<List<Movie>> {
        return repo.getPopularMovieList()
    }

    fun getFavoriteMovieList(): LiveData<List<Movie>> {
        return repo.getFavoriteMovieList()
    }

    fun getFavoriteMovieByID(id: String): Movie? {
        return repo.getFavoriteMovieByID(id)
    }

    fun insertMovie(movie: Movie) {
            repo.insertMovie(movie)
    }

    fun removeMovie(movie: Movie) {
        repo.removeMovie(movie)

    }

    fun getPosition(): LiveData<Int> {
        return rvPosition
    }

    fun setPosition(position:Int){
        rvPosition.value = position
    }

}