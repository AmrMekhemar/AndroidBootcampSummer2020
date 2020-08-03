package com.example.nytarticles.repository

import com.example.nytarticles.BuildConfig.api_key
import com.example.nytarticles.database.ArticlesDAO
import com.example.nytarticles.model.Article
import com.example.nytarticles.networking.RemoteApiService
import kotlinx.coroutines.flow.Flow
import org.koin.core.KoinComponent
import org.koin.core.inject


class ArticleRepo : KoinComponent {
    private val apiService: RemoteApiService by inject()
    private val articlesDAO: ArticlesDAO by inject()
    suspend fun getArticleList(): List<Article> = apiService.getNYTArtsResponse(api_key).results
    fun getArticlesListFromDB(): Flow<List<Article>> = articlesDAO.getArticles()
    fun getFavoriteArticlesListFromDB(): Flow<List<Article>> =
        articlesDAO.getFavoriteArticles(true)

    suspend fun saveArticlesListToDB(articles: List<Article>) =
        articlesDAO.saveArticles(articles)

    suspend fun saveFavoriteArticleToDB(article: Article) =
        articlesDAO.saveFavoriteArticle(article)
}



