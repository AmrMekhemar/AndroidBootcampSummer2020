package com.example.nytarticles.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.nytarticles.getOrAwaitValue
import com.example.nytarticles.model.Article
import com.example.nytarticles.repository.ArticleRepo
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class ArticlesViewModelTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    lateinit var viewModel: ArticlesViewModel

    @Mock
    lateinit var repo: ArticleRepo

    private val articles = listOf(Article("science"), Article("arts"))

    @Before
    fun setup() {
        viewModel = ArticlesViewModel(repo)
    }

    @Test
    fun getArticleList() = coroutinesTestRule.testDispatcher.runBlockingTest {
        whenever(repo.getArticleList()).thenReturn(articles)
        val list = viewModel.getArticleList()
        assertNotNull(list)
    }

    @Test
    fun getArticlesListFromDB() = coroutinesTestRule.testDispatcher.runBlockingTest {
        whenever(repo.getArticlesListFromDB()).thenReturn(flowOf(articles))
        val list = viewModel.getArticlesListFromDB().getOrAwaitValue()
        assertNotNull(list)
    }

    @Test
    fun getFavoriteArticlesListFromDB() = coroutinesTestRule.testDispatcher.runBlockingTest {
        whenever(repo.getFavoriteArticlesListFromDB()).thenReturn(
            flowOf(articles)
        )
        val list = viewModel.getFavoriteArticlesListFromDB().getOrAwaitValue()
        assertNotNull(list)
    }

    @Test
    fun saveArticlesListToDB() = coroutinesTestRule.testDispatcher.runBlockingTest {
        whenever(repo.getArticlesListFromDB()).thenReturn(flowOf(articles))
        viewModel.saveArticlesListToDB(articles)
        val dbArticles = viewModel.getArticlesListFromDB().getOrAwaitValue()
        assertEquals(articles, dbArticles)
    }

    @Test
    fun saveFavoriteArticleToDB() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val articles = listOf(Article(isFavorite = true))
        whenever(repo.getFavoriteArticlesListFromDB()).thenReturn(flowOf(articles))
        viewModel.saveFavoriteArticleToDB((Article(isFavorite = true)))
        val dbFavArticles = viewModel.getFavoriteArticlesListFromDB().getOrAwaitValue()
        assertEquals(articles, dbFavArticles)
    }
}