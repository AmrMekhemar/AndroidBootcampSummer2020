package com.example.nytarticles.viewModel

import androidx.lifecycle.*
import com.example.nytarticles.App.Companion.articleRepo
import com.example.nytarticles.model.Article
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ArticlesViewModel : ViewModel() {
    var list = MutableLiveData<List<Article>>()
    fun getArticleList(): MutableLiveData<List<Article>> {
        viewModelScope.launch(Dispatchers.Main) {
            list.postValue(articleRepo.getArticleList().value)
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

    fun fetchingWithWorkManager() {
        articleRepo.fetchingWithWorkManager()
    }


}
