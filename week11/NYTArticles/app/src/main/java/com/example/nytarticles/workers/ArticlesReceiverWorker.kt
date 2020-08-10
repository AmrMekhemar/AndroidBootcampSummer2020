package com.example.nytarticles.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.nytarticles.App
import com.example.nytarticles.R
import com.example.nytarticles.model.Article
import com.example.nytarticles.repository.ArticleRepo
import com.example.nytarticles.utils.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject

class ArticlesReceiverWorker(
    private val context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters), KoinComponent {
    private val TAG = ArticlesReceiverWorker::class.java.name
    private val repo: ArticleRepo by inject()
    override suspend fun doWork(): Result {
        // retrieving data from internet
        val result: List<Article>? = repo.getArticleList()
        // saving data in the DB
        Log.d(TAG, "results : $result")
        if (result != null) {
            withContext(Dispatchers.Main) {
                context.toast(context.getString(R.string.Syncing_data))
            }
            repo.saveArticlesListToDB(result)
            Log.d(TAG, "saved to db")
        }
        return Result.success()
    }
}