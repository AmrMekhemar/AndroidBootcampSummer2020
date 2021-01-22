package com.example.nytarticles.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import com.example.nytarticles.BuildConfig.api_key
import com.example.nytarticles.database.ArticlesDAO
import com.example.nytarticles.getOrAwaitValue
import com.example.nytarticles.model.Article
import com.example.nytarticles.model.NYTResponse
import com.example.nytarticles.networking.RemoteApiService
import com.example.nytarticles.viewmodel.CoroutineTestRule
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ArticleRepoTest {
    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    @Mock
    lateinit var apiService: RemoteApiService
    @Mock
    lateinit var dao: ArticlesDAO
    private val articles = listOf(Article("science"), Article("arts"))
    lateinit var repo: ArticleRepo

    @Before
    fun setup() {
        repo = ArticleRepo(apiService, dao)
    }

    @Test
    fun getArticleList() = coroutinesTestRule.testDispatcher.runBlockingTest {
        whenever(apiService.getNYTArtsResponse(api_key)).thenReturn(
            NYTResponse("test_Status", "test_copyright", "arts", "4-4-2020", 1, articles)
        )
        val list = repo.getArticleList()
        assertNotNull(list)
    }

    @Test
    fun getArticlesListFromDB() = coroutinesTestRule.testDispatcher.runBlockingTest {
        whenever(dao.getArticles()).thenReturn(flowOf(articles))
        val list = repo.getArticlesListFromDB().asLiveData().getOrAwaitValue()
        assertNotNull(list)
    }

    @Test
    fun getFavoriteArticlesListFromDB() =coroutinesTestRule.testDispatcher.runBlockingTest {
        whenever(dao.getFavoriteArticles(true)).thenReturn(flowOf(articles))
        val list = repo.getFavoriteArticlesListFromDB().asLiveData().getOrAwaitValue()
        assertNotNull(list)
    }

    @Test
    fun saveArticlesListToDB() = coroutinesTestRule.testDispatcher.runBlockingTest {
        whenever(dao.getArticles()).thenReturn(flowOf(articles))
        repo.saveArticlesListToDB(articles)
        val dbArticles = repo.getArticlesListFromDB().asLiveData().getOrAwaitValue()
        assertEquals(articles, dbArticles)
    }

    @Test
    fun saveFavoriteArticleToDB() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val articles = listOf(Article(isFavorite = true))
        whenever(dao.getFavoriteArticles(true)).thenReturn(flowOf(articles))
        repo.saveFavoriteArticleToDB(Article(isFavorite = true))
        val dbArticles = repo.getFavoriteArticlesListFromDB().asLiveData().getOrAwaitValue()
        assertEquals(articles, dbArticles)
    }
}