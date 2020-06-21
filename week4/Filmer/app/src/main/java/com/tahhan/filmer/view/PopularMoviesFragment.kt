package com.tahhan.filmer.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.tahhan.filmer.R
import com.tahhan.filmer.utils.MovieAdapter
import com.tahhan.filmer.viewmodel.MovieViewModel
import kotlinx.android.synthetic.main.fragment_popular_movies.*


/**
 * A simple [Fragment] subclass.
 * Use the [PopularMoviesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PopularMoviesFragment : Fragment() {
    companion object {
        @JvmStatic
        fun newInstance() =
            PopularMoviesFragment()
    }

    lateinit var movieViewModel: MovieViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_popular_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel::class.java)
        movieViewModel.getMovies().observe(this.viewLifecycleOwner, Observer {
            movieRV.adapter = MovieAdapter(it) { movieID ->
                Toast.makeText(context, "position is: $movieID", Toast.LENGTH_SHORT).show()
                val action = PopularMoviesFragmentDirections.actionPopularMoviesToDetails(movieID)
                view.findNavController().navigate(action)
            }
        })
        movieRV.layoutManager = GridLayoutManager(context, 2)
        favoritesFab.setOnClickListener {
            view.findNavController().navigate(R.id.action_popularMovies_to_favorites)
        }


    }


}