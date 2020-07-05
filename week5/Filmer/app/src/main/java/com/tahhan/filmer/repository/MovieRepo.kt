package com.tahhan.filmer.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tahhan.filmer.Constants
import com.tahhan.filmer.database.MovieDB
import com.tahhan.filmer.model.Movie
import com.tahhan.filmer.model.MovieResponse
import com.tahhan.filmer.networking.MyWebService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieRepo(application: Application) {
    companion object {
        val TAG = MovieResponse::class.java.name
    }

    var movieList: MutableLiveData<List<Movie>>? = null
    var movie : MutableLiveData<Movie>? = MutableLiveData()

    private var database: MovieDB = MovieDB.getDatabase(application)

    fun getFavoriteMovieList(): LiveData<List<Movie>> {
        return database.movieDao().loadFavoriteMovies()
    }

    fun getFavoriteMovieByID(id: String): MutableLiveData<Movie>? {
        GlobalScope.launch {
            movie?.postValue(database.movieDao().loadMovieById(id))
        }
        return movie

    }

    fun insertMovie(movie: Movie) {
            database.movieDao().insertMovie(movie)
    }

    fun removeMovie(movie: Movie) {

            database.movieDao().deleteMovie(movie)


    }


    fun getPopularMovieList(): MutableLiveData<List<Movie>> {
        movieList = MutableLiveData()
        requestPopularMovies()
        return movieList!!
    }

    private fun requestPopularMovies() {
        val mService = MyWebService.retrofit.create(MyWebService::class.java)
        mService.discoverPopularMovie(Constants.MOVIE_DB_API_KEY)!!
            .enqueue(object : Callback<MovieResponse?> {
                override fun onFailure(call: Call<MovieResponse?>, t: Throwable) {
                    Log.d(TAG, "message: $t")
                }

                override fun onResponse(
                    call: Call<MovieResponse?>,
                    response: Response<MovieResponse?>
                ) {
                    val movieResponse = response.body()
                    if (movieResponse != null)
                        movieList!!.postValue(movieResponse.results)
                    else
                        movieList?.postValue(null)
                }

            })
    }
}