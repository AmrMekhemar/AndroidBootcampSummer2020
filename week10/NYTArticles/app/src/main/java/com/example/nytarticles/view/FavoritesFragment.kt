package com.example.nytarticles.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nytarticles.R
import com.example.nytarticles.model.Article
import com.example.nytarticles.networking.NetworkStatusChecker
import com.example.nytarticles.utils.ArticleAdapter
import com.example.nytarticles.utils.toast
import com.example.nytarticles.viewmodel.ArticlesViewModel
import kotlinx.android.synthetic.main.fragment_favorites.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * A simple Fragment to retrieve favorite articles from the db
 * and display them in a recyclerview
 */
class FavoritesFragment : Fragment() {
    private val FAVORITES_SCROLL_KEY = "favorites_scroll_key"
    private val layoutManager: LinearLayoutManager by inject()
    private val networkStatusChecker: NetworkStatusChecker by inject()
    private val articlesViewModel: ArticlesViewModel by viewModel()

    // Inflate the layout for this fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchFavoritesFromDB()
        restoreRvState(savedInstanceState)
    }

    /**
     * restoring the state of the recyclerview
     */
    private fun restoreRvState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            favoritesRV.layoutManager = layoutManager
            favoritesRV.scrollToPosition(savedInstanceState.getInt(FAVORITES_SCROLL_KEY))
        }
    }

    /**
     * Fetch favorite articles from DB
     */
    private fun fetchFavoritesFromDB() {
        articlesViewModel.getFavoriteArticlesListFromDB()
            .observe(this.viewLifecycleOwner, Observer {
                setupRecyclerView(it)
            })
    }

    /**
     * attaching the layoutManager and the adapter to the recyclerview
     */
    private fun setupRecyclerView(articles: List<Article>) {
        favoritesRV.layoutManager = layoutManager
        favoritesRV.adapter = ArticleAdapter(articles, listener = {
            if (it is Article) {
                if (!it.isFavorite) {
                    it.isFavorite = true
                    articlesViewModel.saveFavoriteArticleToDB(it)
                    requireContext().toast(getString(R.string.added_to_favorites))
                } else {
                    it.isFavorite = false
                    articlesViewModel.saveFavoriteArticleToDB(it)
                    requireContext().toast(getString(R.string.removed_from_favorites))
                }

            } else {
                networkStatusChecker.performIfConnectedToInternet {
                    val action = FavoritesFragmentDirections.actionFavoritesToWebView(it as String)
                    favoritesRV.layoutManager = null
                    findNavController().navigate(action)
                }
            }
        })
    }

    /**
     * saving the state of the recyclerView
     */
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(
            FAVORITES_SCROLL_KEY,
            layoutManager.findLastCompletelyVisibleItemPosition()
        )
        super.onSaveInstanceState(outState)
    }

    /**
     * unbinding the layout manager
     */
    override fun onPause() {
        super.onPause()
        favoritesRV.layoutManager = null
    }

}
