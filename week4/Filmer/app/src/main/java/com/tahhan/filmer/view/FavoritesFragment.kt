package com.tahhan.filmer.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.tahhan.filmer.R
import com.tahhan.filmer.utils.MovieAdapter
import com.tahhan.filmer.viewmodel.MovieViewModel
import kotlinx.android.synthetic.main.fragment_favorites.*
import kotlinx.android.synthetic.main.fragment_popular_movies.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [FavoritesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavoritesFragment : Fragment() {
      lateinit var movieViewModel: MovieViewModel
    companion object {
        @JvmStatic
        fun newInstance() =
            FavoritesFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel::class.java)
        favoritesRV.layoutManager = GridLayoutManager(context,2)
        movieViewModel.getFavoriteMovieList().observe(this.viewLifecycleOwner, Observer {
            favoritesRV.adapter = MovieAdapter(it) { movieID ->
                Toast.makeText(context, "position is: $movieID", Toast.LENGTH_SHORT).show()
                val action = PopularMoviesFragmentDirections.actionPopularMoviesToDetails(movieID)
                view.findNavController().navigate(action)
            }
        })

    }


}