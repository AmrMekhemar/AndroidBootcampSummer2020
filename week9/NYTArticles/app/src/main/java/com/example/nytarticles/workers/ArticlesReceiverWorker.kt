package com.example.nytarticles.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.nytarticles.App
import com.example.nytarticles.R
import com.example.nytarticles.model.Article
import com.example.nytarticles.utils.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ArticlesReceiverWorker(
    val context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {
    private val TAG = ArticlesReceiverWorker::class.java.name
    private val repo = App.articleRepo
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