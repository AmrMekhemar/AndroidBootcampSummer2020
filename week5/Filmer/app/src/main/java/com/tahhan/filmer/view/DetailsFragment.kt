package com.tahhan.filmer.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import com.tahhan.filmer.Constants
import com.tahhan.filmer.R
import com.tahhan.filmer.model.Movie
import com.tahhan.filmer.viewmodel.MovieViewModel
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


/**
 * A simple [DetailsFragment]
 * to display the details of a movie
 */
class DetailsFragment : Fragment() {
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var args: DetailsFragmentArgs
    var movie: Movie? = null
    private var movieLiveData = MutableLiveData<Movie>()
    private var isSavedLiveData = MutableLiveData<Boolean>()

    // Inflate the layout for this fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel::class.java)
        arguments?.let {
            args = DetailsFragmentArgs.fromBundle(it)

        }
        populateData()
        movie = movieLiveData.value
        fab.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                if (isSavedLiveData.value == false) {
                    movieViewModel.insertMovie(movie!!)
                    Snackbar.make(
                        view.rootView,
                        getString(R.string.movie_inserted),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    fab.setImageDrawable(resources.getDrawable(R.drawable.delete))
                    isSavedLiveData.postValue(true)

                } else {
                    movieViewModel.removeMovie(movie!!)
                    Snackbar.make(
                        view.rootView,
                        getString(R.string.movie_removed),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    fab.setImageDrawable(resources.getDrawable(R.drawable.heart))
                    isSavedLiveData.postValue(false)
                }

            }
        }
    }

    /**
     * A function to retrieve data from the database or the api
     * depending on whether the movie is saved on the database or not
     */
    private fun populateData() {
        movieViewModel.getFavoriteMovieByID(args.movieID.toString())
            ?.observe(this.viewLifecycleOwner,
                Observer {
                    fillData(it)
                    movie = it
                    fab.setImageDrawable(resources.getDrawable(R.drawable.delete))
                    fillData(movie)
                    if (movie == null) {
                        movieViewModel.getMovies()
                            .observe(this.viewLifecycleOwner, Observer { movieList ->
                                movie = movieList.find { movie ->
                                    movie.id == args.movieID
                                }
                                fab.setImageDrawable(resources.getDrawable(R.drawable.heart))
                                fillData(movie)

                            })
                        isSavedLiveData.postValue(false)
                    }
                    else {
                        movieLiveData.postValue(movie)
                        isSavedLiveData.postValue(true)

                    }
                }

            )


    }

    /**
     * A function to fill the view with the movie details
     * @param movie
     */
    private fun fillData(movie: Movie?) {
        if (movie != null) {
            plot_synopsis.text = movie.overview
            original_title.text = movie.title
            user_rating.text = movie.vote_average.toString()
            release_date.text = movie.release_date
            Picasso.get().load(Constants.ROOT_BACKDROP_IMAGE_URL + movie.backdrop_path)
                .error(R.color.colorPrimary).placeholder(R.color.colorAccent).into(backdrop_ip)
        }


    }

}
