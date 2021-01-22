package com.example.nytarticles

import android.app.Application
import androidx.room.Room
import com.example.nytarticles.database.ArticleDB
import com.example.nytarticles.database.ArticlesDAO
import com.example.nytarticles.repository.ArticleRepo
import com.example.nytarticles.networking.buildApiService
import com.example.nytarticles.viewmodel.ArticlesViewModel
import splitties.init.appCtx

class App : Application() {
    companion object {
        private lateinit var appInstance: App
        fun getAppContext() = appInstance
        private val articleDb: ArticleDB by lazy {
            Room.databaseBuilder(appCtx, ArticleDB::class.java, DATABASE_NAME)
                .build()
        }


        private val articlesDAO: ArticlesDAO by lazy {
            articleDb.articleDao()
        }
        val BASE_URL = "https://api.nytimes.com/svc/topstories/v2/"
        private val apiService by lazy { buildApiService() }
        val articleRepo by lazy { ArticleRepo(apiService, articlesDAO) }
        val viewModelFactory by lazy { ArticlesViewModel(articleRepo) }
        val DATABASE_NAME = "article_database"
    }

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }
}