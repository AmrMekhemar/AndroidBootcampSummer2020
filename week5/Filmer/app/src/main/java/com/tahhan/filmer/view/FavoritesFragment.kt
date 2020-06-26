package com.tahhan.filmer.view

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import androidx.recyclerview.widget.RecyclerView
import com.tahhan.filmer.R
import com.tahhan.filmer.utils.MovieAdapter
import com.tahhan.filmer.viewmodel.MovieViewModel
import kotlinx.android.synthetic.main.fragment_favorites.*

/**
 * A simple [FavoritesFragment] Fragment.
 * retrieves the Favorite movies from the database
 * an show them in a recycler view
 */
class FavoritesFragment : Fragment() {
    lateinit var movieViewModel: MovieViewModel
    private val layoutManager: GridLayoutManager by lazy {
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            val lm = GridLayoutManager(context, 3)
            lm.isUsingSpansToEstimateScrollbarDimensions
            lm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (position == 0) 3 else 1
                }
            }
            lm
        } else {
            GridLayoutManager(context, 5)
        }
    }

    companion object {
        const val FAVORITE_SCROLL_KEY = "fav_scroll_key"
        val TAG = FavoritesFragment::class.java.name
    }

    /**
     * A simple [SimpleCallback] object.
     * @param [dragDirs] for drag directions
     * @param [swipDirs] for swip directions
     * @param lambda expression which is triggered when an item is moved or swiped
     */
    private var simpleItemTouchCallback: SimpleCallback = object :
        SimpleCallback(
            0,
            ItemTouchHelper.LEFT
        ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            Toast.makeText(context, getString(R.string.on_move), Toast.LENGTH_SHORT).show()
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
            Toast.makeText(context, getString(R.string.movie_removed), Toast.LENGTH_SHORT).show()
            val position = viewHolder.adapterPosition
            val adapter = favoritesRV.adapter as MovieAdapter
            // Remove swiped item from the Database
            movieViewModel.removeMovie(adapter.movieList[position])
        }
    }

    // Inflate the layout for this fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retrieveMovies(view)
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(favoritesRV)
        Toast.makeText(context, getString(R.string.swipe_left_unfavorite), Toast.LENGTH_SHORT)
            .show()

        if (savedInstanceState != null && savedInstanceState.getInt(FAVORITE_SCROLL_KEY) != 0) {
            favoritesRV.scrollToPosition(savedInstanceState.getInt(FAVORITE_SCROLL_KEY))
            Log.d(
                PopularMoviesFragment.TAG, "scrolled to position: ${savedInstanceState.getInt(
                    PopularMoviesFragment.SCROLL_KEY
                )}"
            )
        }
    }

    //instantiating the viewModel and observing the favorite movies
    private fun retrieveMovies(view: View) {
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel::class.java)
        favoritesRV.layoutManager = layoutManager
        movieViewModel.getFavoriteMovieList().observe(this.viewLifecycleOwner, Observer {
            favoritesRV.adapter = MovieAdapter(it) { movieID ->
                favoritesRV.layoutManager = null
                navigateToDetailsFragment(movieID, view)
            }
        })
    }

    // navigate to DetailsFragment
    private fun navigateToDetailsFragment(movieID: Int, view: View) {
        val action = FavoritesFragmentDirections.actionFavoritesToDetails(movieID)
        view.findNavController().navigate(action)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(
            FAVORITE_SCROLL_KEY,
            layoutManager.findLastCompletelyVisibleItemPosition()
        )
        Log.d(
            PopularMoviesFragment.TAG,
            "onSaved: scroll position ${layoutManager.findLastCompletelyVisibleItemPosition()}"
        )
        super.onSaveInstanceState(outState)
    }


}