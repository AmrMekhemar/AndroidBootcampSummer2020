package com.example.nytarticles

import android.app.Application
import com.example.nytarticles.di.articleRepoModule
import com.example.nytarticles.di.presentationModule
import com.example.nytarticles.di.userRepoModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {
    companion object {
        val BASE_URL = "https://api.nytimes.com/svc/topstories/v2/"
        val DATABASE_NAME = "article_database"
    }
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(listOf(articleRepoModule, userRepoModule, presentationModule))
        }
    }
}