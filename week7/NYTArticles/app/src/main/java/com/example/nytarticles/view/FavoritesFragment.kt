package com.example.nytarticles.view

import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nytarticles.R
import com.example.nytarticles.model.Article
import com.example.nytarticles.networking.NetworkStatusChecker
import com.example.nytarticles.utils.ArticleAdapter
import com.example.nytarticles.utils.toast
import com.example.nytarticles.viewModel.ArticlesViewModel
import kotlinx.android.synthetic.main.fragment_favorites.*


/**
 * A simple Fragment to retrieve favorite articles from the db
 * and display them in a recyclerview
 */
class FavoritesFragment : Fragment() {
    private val FAVORITES_SCROLL_KEY = "favorites_scroll_key"
    private val layoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    private val networkStatusChecker: NetworkStatusChecker by lazy {
        NetworkStatusChecker(requireContext().getSystemService(ConnectivityManager::class.java))
    }
    private val articlesViewModel: ArticlesViewModel by lazy {
        ViewModelProvider(this).get(ArticlesViewModel::class.java)
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
        fetchFavoritesFromDB()
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


}
