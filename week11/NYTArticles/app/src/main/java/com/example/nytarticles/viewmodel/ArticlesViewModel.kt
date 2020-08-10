package com.example.nytarticles.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.nytarticles.model.Article
import com.example.nytarticles.repository.ArticleRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ArticlesViewModel(private val articleRepo:ArticleRepo) : ViewModel() {
    var list = listOf<Article>()
    fun getArticleList(): List<Article> {
        viewModelScope.launch(Dispatchers.Main) {
            list = articleRepo.getArticleList()
        }
        return list
    }

    fun getArticlesListFromDB(): LiveData<List<Article>> =
        articleRepo.getArticlesListFromDB().asLiveData()

    fun getFavoriteArticlesListFromDB(): LiveData<List<Article>> =
        articleRepo.getFavoriteArticlesListFromDB().asLiveData()


    fun saveArticlesListToDB(articles: List<Article>) = viewModelScope.launch {
        articleRepo.saveArticlesListToDB(articles)
    }

    fun saveFavoriteArticleToDB(article: Article) = viewModelScope.launch {
        articleRepo.saveFavoriteArticleToDB(article)
    }
}
