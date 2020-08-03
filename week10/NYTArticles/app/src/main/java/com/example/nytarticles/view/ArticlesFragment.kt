package com.example.nytarticles.view

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
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
import com.example.nytarticles.viewmodel.UserViewModel
import com.example.nytarticles.workers.WorkerUtils
import kotlinx.android.synthetic.main.fragment_articles.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * A simple Fragment to retrieve and store the articles from the NYT Api
 * and display them in a recyclerview
 */
class ArticlesFragment : Fragment() {
    private val ARTICLES_SCROLL_KEY = "art_scroll_key"
    private val TAG = ArticlesFragment::class.java.name
    private val layoutManager: LinearLayoutManager by inject()
    private val networkStatusChecker: NetworkStatusChecker by inject()
    private val articlesViewModel : ArticlesViewModel by viewModel()
    private val userViewModel : UserViewModel by viewModel()


    // Inflate the layout for this fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_articles, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        fetchDataFromInternet()
        fetchDataFromDB()
        WorkerUtils.synchronizeArticles()
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
            val articles = articlesViewModel.getArticleList()
            Log.d(TAG, "internet data = $articles")
            articlesViewModel.saveArticlesListToDB(articles)
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

    override fun onPause() {
        super.onPause()
        articlesRV.layoutManager = null
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

    private fun signOut() {
        userViewModel.logout()
        findNavController().navigate(ArticlesFragmentDirections.actionArticlesToLogin())
    }

    // a function to instantiate an alert dialog
    private fun initDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.user_info))
        builder.setMessage(
            "user name: ${userViewModel.getUserName()}\n" +
                    "email: ${userViewModel.getUserEmail()} "
        ).show()
    }

}