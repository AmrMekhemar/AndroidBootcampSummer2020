package com.example.nytarticles.workers

import androidx.work.*
import splitties.init.appCtx
import java.util.concurrent.TimeUnit

class WorkerUtils {
    companion object{
        fun synchronizeArticles() {
            val constraints = Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .setRequiredNetworkType(NetworkType.NOT_ROAMING)
                .setRequiresStorageNotLow(true)
                .build()

            val articlesReceiverWorker =
                PeriodicWorkRequestBuilder<ArticlesReceiverWorker>(15, TimeUnit.MINUTES)
                    .setConstraints(constraints)
                    .build()

            val workerManager = WorkManager.getInstance(appCtx)
            workerManager.enqueueUniquePeriodicWork(
                "worker",
                ExistingPeriodicWorkPolicy.KEEP,
                articlesReceiverWorker
            )

        }
    }
}