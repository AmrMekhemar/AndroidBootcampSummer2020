package com.example.nytarticles.repository

import androidx.lifecycle.MutableLiveData
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.nytarticles.App
import com.example.nytarticles.BuildConfig.api_key
import com.example.nytarticles.database.ArticlesDAO
import com.example.nytarticles.model.Article
import com.example.nytarticles.networking.RemoteApiService
import com.example.nytarticles.workers.ArticlesReceiverWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit


class ArticleRepo(private val apiService: RemoteApiService, private val articlesDAO: ArticlesDAO) {
    private var resultList: MutableLiveData<List<Article>> = MutableLiveData()
    val TAG = ArticleRepo::class.java.name

    suspend fun getArticleList(): MutableLiveData<List<Article>> =
        withContext(Dispatchers.IO) {
            val results = apiService.getNYTArtsResponse(api_key).results
            resultList.postValue(results)
            resultList
        }

    fun getArticlesListFromDB(): Flow<List<Article>> = articlesDAO.getArticles()

    fun getFavoriteArticlesListFromDB(): Flow<List<Article>> =
        articlesDAO.getFavoriteArticles(true)

    suspend fun saveArticlesListToDB(articles: List<Article>) =
        articlesDAO.saveArticles(articles)

    suspend fun saveFavoriteArticleToDB(article: Article) =
        articlesDAO.saveFavoriteArticle(article)

    fun fetchingWithWorkManager() {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiredNetworkType(NetworkType.NOT_ROAMING)
            .setRequiresStorageNotLow(true)
            .build()

        val articlesReceiverWorker =
            PeriodicWorkRequestBuilder<ArticlesReceiverWorker>(1, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build()

        val workerManager = WorkManager.getInstance(App.getAppContext())
        workerManager.enqueue(articlesReceiverWorker)
    }
}



