package com.example.nytarticles.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nytarticles.repository.ArticleRepo

class ArticlesViewModelFactory(val articleRepo: ArticleRepo):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
       return ArticlesViewModel(articleRepo ) as T
    }
}