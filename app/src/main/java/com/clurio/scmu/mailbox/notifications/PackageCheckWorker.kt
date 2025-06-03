package com.clurio.scmu.mailbox.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.clurio.scmu.mailbox.R
import com.clurio.scmu.mailbox.notifications.NotificationHandler.showNotification
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class PackageCheckWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    companion object {
        private const val TAG = "PackageCheckWorker"
        private const val FOREGROUND_CHANNEL_ID = "foreground_channel"
        private const val NOTIFICATION_ID = 1
        private const val SHARED_PREFS_NAME = "prefs"
        private const val PREF_KEY_PACKAGES = "packages"
        private const val FIREBASE_URL =
            "https://mailbox-movel-default-rtdb.europe-west1.firebasedatabase.app"
    }

    private val database = Firebase.database(url = FIREBASE_URL)

    override suspend fun doWork(): Result {
        setForeground(createForegroundInfo())

        return try {
            val ref = database.getReference("/lastPackageNumber")
            val snapshot = ref.get().await()
            val currentCount = snapshot.getValue(Int::class.java) ?: 0

            val prefs =
                applicationContext.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
            val previousCount = prefs.getInt(PREF_KEY_PACKAGES, 0)

            if (currentCount > previousCount) {
                Log.d(TAG, "Package count increased: $previousCount → $currentCount")
                showNotification(
                    context = applicationContext,
                    message = "You have $currentCount packages."
                )
                prefs.edit().putInt(PREF_KEY_PACKAGES, currentCount).apply()
            }

            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Error checking packages", e)
            Result.retry()
        }
    }

    private fun createForegroundInfo(): ForegroundInfo {
        val manager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        ensureNotificationChannel(manager)

        val notification = NotificationCompat.Builder(applicationContext, FOREGROUND_CHANNEL_ID)
            .setContentTitle("Checking for packages")
            .setContentText("Running background task")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ForegroundInfo(
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )
        } else {
            ForegroundInfo(NOTIFICATION_ID, notification)
        }
    }

    private fun ensureNotificationChannel(
        manager: NotificationManager,
    ) {
        val channel = NotificationChannel(
            FOREGROUND_CHANNEL_ID,
            "Foreground Worker Channel",
            NotificationManager.IMPORTANCE_LOW
        )
        manager.createNotificationChannel(channel)
    }
}
