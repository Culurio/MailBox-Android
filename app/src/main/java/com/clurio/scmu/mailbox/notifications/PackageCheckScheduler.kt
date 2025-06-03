package com.clurio.scmu.mailbox.notifications

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object PackageCheckScheduler {
    fun schedule(context: Context) {
        val workRequest = PeriodicWorkRequestBuilder<PackageCheckWorker>(15, TimeUnit.MINUTES).build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "package-checker",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}
