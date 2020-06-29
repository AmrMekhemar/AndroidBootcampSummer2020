package com.tahhan.filmer.view

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.google.firebase.auth.FirebaseAuth
import com.tahhan.filmer.R
import com.tahhan.filmer.utils.MovieAdapter
import com.tahhan.filmer.view.MainActivity.Companion.sharedPref
import com.tahhan.filmer.viewmodel.MovieViewModel
import kotlinx.android.synthetic.main.fragment_popular_movies.*


/**
 * A simple [PopularMoviesFragment] .
 * to display a recyclerView of Popular Movies
 */
class PopularMoviesFragment : Fragment() {
    companion object {
        const val SCROLL_KEY = "scrollkey"
        val TAG = PopularMoviesFragment::class.java.name
    }

    private val layoutManager: GridLayoutManager by lazy {
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            val lm = GridLayoutManager(context, 3)
            lm.isUsingSpansToEstimateScrollbarDimensions
            lm.spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (position == 0) 3 else 1
                }
            }
            lm
        } else {
            GridLayoutManager(context, 5)
        }

    }

    lateinit var movieViewModel: MovieViewModel

    // Inflate the layout for this fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_popular_movies, container, false)
    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        movieRV.layoutManager = layoutManager
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel::class.java)
        movieViewModel.getMovies().observe(this.viewLifecycleOwner, Observer {
            movieRV.adapter = MovieAdapter(it) { movieID ->
                val action = PopularMoviesFragmentDirections.actionPopularMoviesToDetails(movieID)
                movieRV.layoutManager = null
                view.findNavController().navigate(action)

            }

        })

        favoritesFab.setOnClickListener {
            movieRV.layoutManager = null
            view.findNavController().navigate(R.id.action_popularMovies_to_favorites)

        }
        if (savedInstanceState != null && savedInstanceState.getInt(SCROLL_KEY) != 0) {
            movieRV.scrollToPosition(savedInstanceState.getInt(SCROLL_KEY))
            Log.d(TAG, "scrolled to position: ${savedInstanceState.getInt(SCROLL_KEY)}")
        }



    }

    private fun setupToolbar() {
        toolbar.inflateMenu(R.menu.menu)
        toolbar.setOnMenuItemClickListener { menuItem ->

            when (menuItem.itemId) {
                R.id.item_user_info -> initDialog()
                R.id.item_sign_out -> signOut()
            }
            true
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(SCROLL_KEY, layoutManager.findLastCompletelyVisibleItemPosition())
        Log.d(
            TAG,
            "onSaved: scroll position ${layoutManager.findLastCompletelyVisibleItemPosition()}"
        )
        super.onSaveInstanceState(outState)
    }


    // a function to instantiate an alert dialog
    private fun initDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.user_info))
        builder.setMessage(
            "user name: ${LoginFragment.mAuth.currentUser!!.displayName}\n" +
                    "email: ${LoginFragment.mAuth.currentUser!!.email} "
        ).show()
    }

    private fun signOut() {
        LoginFragment.mAuth.signOut()
        findNavController().navigate(PopularMoviesFragmentDirections.actionPopularMoviesToLogin())
        with(sharedPref.edit()) {
            putBoolean(getString(R.string.login_state_key), false)
            commit()
        }

    }

}