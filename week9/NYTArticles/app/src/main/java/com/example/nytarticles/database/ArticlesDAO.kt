package com.example.nytarticles.database
import androidx.room.*
import androidx.room.OnConflictStrategy.*
import com.example.nytarticles.model.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticlesDAO {

    @Insert(onConflict = IGNORE)
    suspend fun saveArticles(articles: List<Article>)

    @Insert(onConflict = REPLACE)
    suspend fun saveFavoriteArticle(article: Article)


    @Query("Select * From Articles where isFavorite = :isFavorite")
    fun getFavoriteArticles(isFavorite: Boolean): Flow<List<Article>>

    @Query("Select * From Articles")
    fun getArticles(): Flow<List<Article>>
}
