package com.tahhan.filmer.view

import android.content.res.Configuration
import android.nfc.Tag
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.tahhan.filmer.R
import com.tahhan.filmer.utils.MovieAdapter
import com.tahhan.filmer.viewmodel.MovieViewModel
import kotlinx.android.synthetic.main.fragment_popular_movies.*


/**
 * A simple [PopularMoviesFragment] .
 * to display a recyclerView of Popular Movies
 */
class PopularMoviesFragment : Fragment() {
    companion object {
        @JvmStatic
        fun newInstance() =
            PopularMoviesFragment()
        const val SCROLL_KEY = "scrollkey"
        val TAG = PopularMoviesFragment::class.java.name
    }

    lateinit var layoutManager: GridLayoutManager

    lateinit var movieViewModel: MovieViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_popular_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayoutManager()
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel::class.java)
        movieViewModel.getMovies().observe(this.viewLifecycleOwner, Observer {
            movieRV.adapter = MovieAdapter(it) { movieID ->
                val action = PopularMoviesFragmentDirections.actionPopularMoviesToDetails(movieID)
                view.findNavController().navigate(action)
            }



        })

        favoritesFab.setOnClickListener {
            view.findNavController().navigate(R.id.action_popularMovies_to_favorites)
        }
//        if (savedInstanceState != null && savedInstanceState.getInt(SCROLL_KEY) != 0) {
//            movieRV.scrollToPosition(savedInstanceState.getInt(SCROLL_KEY))
//            Log.d(TAG, "scrolled")
//        }
    }


    private fun setLayoutManager() {
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = GridLayoutManager(context, 3)
            layoutManager.isUsingSpansToEstimateScrollbarDimensions
            layoutManager.spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (position == 0) 3 else 1
                }
            }
            movieRV.layoutManager = layoutManager
        } else {
            movieRV.layoutManager = GridLayoutManager(context, 5)
        }


    }


//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        val scrollPosition:Int? = (movieRV.layoutManager as GridLayoutManager).findLastVisibleItemPosition()
//        outState.putInt(SCROLL_KEY, scrollPosition!!)
//        Log.d(TAG, "onSaved: scroll position $scrollPosition")
//    }

}