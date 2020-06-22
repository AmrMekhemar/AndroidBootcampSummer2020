package com.tahhan.filmer.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import com.tahhan.filmer.Constants
import com.tahhan.filmer.R
import com.tahhan.filmer.model.Movie
import com.tahhan.filmer.viewmodel.MovieViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


/**
 * A simple [DetailsFragment]
 * to display the details of a movie
 */
class DetailsFragment : Fragment() {
    lateinit var movieViewModel: MovieViewModel
    private lateinit var args: DetailsFragmentArgs
    private lateinit var fab: FloatingActionButton
    var movie: Movie? = null


    companion object {
        @JvmStatic
        fun newInstance() = DetailsFragment()
        val TAG = DetailsFragment::class.java.name
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel::class.java)
        arguments?.let {
            args = DetailsFragmentArgs.fromBundle(it)

        }
        fab = view.findViewById(R.id.fab)
        movieViewModel.getMovies().observe(this.viewLifecycleOwner, Observer { movieList ->
            movie = movieList.find { movie ->
                movie.id == args.movieID
            }
            setFabState()
            populateData(movie!!)
        })
        fab.setOnClickListener {
            GlobalScope.launch {
                val flag = movieViewModel.getFavoriteMovieByID(args.movieID.toString())
                if (flag == null) {
                    movieViewModel.insertMovie(movie!!)
                    Snackbar.make(
                        view.rootView,
                        getString(R.string.movie_inserted),
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    movieViewModel.removeMovie(movie!!)
                    Snackbar.make(
                        view.rootView,
                        getString(R.string.movie_removed),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                setFabState()
            }
        }
    }


    private fun populateData(movie: Movie) {
        view?.findViewById<TextView>(R.id.plot_synopsis)!!.text = movie.overview
        view?.findViewById<TextView>(R.id.original_title)!!.text = movie.title
        view?.findViewById<TextView>(R.id.user_rating)!!.text = movie.vote_average.toString()
        view?.findViewById<TextView>(R.id.release_date)!!.text = movie.release_date
        val backdrop = view?.findViewById<ImageView>(R.id.backdrop_ip)
        Picasso.get().load(Constants.ROOT_BACKDROP_IMAGE_URL + movie.backdrop_path)
            .error(R.color.colorPrimary).placeholder(R.color.colorAccent).into(backdrop)

    }

    private fun setFabState() {
        GlobalScope.launch {
            val movie = movieViewModel.getFavoriteMovieByID(args.movieID.toString())
            if (movie == null) {
                fab.setImageDrawable(resources.getDrawable(R.drawable.heart))
            } else
                fab.setImageDrawable(resources.getDrawable(R.drawable.delete))
        }
    }

}
