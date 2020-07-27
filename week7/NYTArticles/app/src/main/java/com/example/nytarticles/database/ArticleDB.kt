package com.example.nytarticles.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.nytarticles.model.Article
import com.example.nytarticles.model.MultiMediaConverter
import com.example.nytarticles.model.Multimedia


@Database(entities = [Article::class,Multimedia::class], version = 1, exportSchema = false)
@TypeConverters(MultiMediaConverter::class)
abstract class ArticleDB : RoomDatabase() {
    abstract fun articleDao(): ArticlesDAO
}