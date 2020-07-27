package com.example.nytarticles.view

import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
import kotlinx.android.synthetic.main.fragment_articles.*


/**
 * A simple Fragment to retrieve and store the articles from the NYT Api
 * and display them in a recyclerview
 */
class ArticlesFragment : Fragment() {
    private val ARTICLES_SCROLL_KEY = "art_scroll_key"
    private val TAG = ArticlesFragment::class.java.name
    private val notificationId = "101"
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
        return inflater.inflate(R.layout.fragment_articles, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchDataFromInternet()
        fetchDataFromDB()
        articlesViewModel.fetchingWithWorkManager()
        fab.setOnClickListener {
            launchingFavoritesFragment()
        }
        restoreRvState(savedInstanceState)

    }

    /**
     * restoring the state of the recyclerview
     */
    private fun restoreRvState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            articlesRV.layoutManager = layoutManager
            articlesRV.scrollToPosition(savedInstanceState.getInt(ARTICLES_SCROLL_KEY))
            Log.d(TAG, "position: ${savedInstanceState.getInt(ARTICLES_SCROLL_KEY)}")
        }
    }

    /**
     * navigating to FavoritesFragment if There is favorite movies
     */
    private fun launchingFavoritesFragment() {
        articlesViewModel.getFavoriteArticlesListFromDB()
            .observe(this.viewLifecycleOwner, Observer {
                if (it.isEmpty())
                    requireContext().toast(getString(R.string.no_favorite_articles))
                else {
                    articlesRV.layoutManager = null
                    findNavController().navigate(ArticlesFragmentDirections.actionArticlesToFavorites())
                }

            })
    }


    /**
     * Fetching articles from the Database if the internet is not connected
     */

    private fun fetchDataFromDB() {
        articlesViewModel.getArticlesListFromDB().observe(this.viewLifecycleOwner, Observer {
            Log.d(TAG, "it = $it")
            setupRecyclerView(it)
        })

    }

    /**
     * Fetching articles from the internet
     */
    private fun fetchDataFromInternet() {
        networkStatusChecker.performIfConnectedToInternet {
            articlesViewModel.getArticleList().observe(this.viewLifecycleOwner, Observer {
                Log.d(TAG, "internet data = $it")
                articlesViewModel.saveArticlesListToDB(it)
            })
        }
    }


    /**
     * attaching the layoutManager and the adapter to the recyclerview
     */
    private fun setupRecyclerView(articles: List<Article>) {
        articlesRV.layoutManager = layoutManager
        articlesRV.adapter = ArticleAdapter(articles, listener = {
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
                    articlesRV.layoutManager = null
                    val action = ArticlesFragmentDirections.actionArticlesToWebView(it as String)
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
            ARTICLES_SCROLL_KEY,
            layoutManager.findLastCompletelyVisibleItemPosition()
        )
        super.onSaveInstanceState(outState)

    }


}