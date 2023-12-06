package com.example.glance

import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.glance.ui.ChangeStateWorker

class SyncService : JobIntentService() {
    init {
        Log.i("SyncService", "init!")
    }

    override fun onHandleWork(intent: Intent) {
        startForegroundService(intent)

        val work = OneTimeWorkRequest.Builder(ChangeStateWorker::class.java)
            .build()

        WorkManager.getInstance(baseContext).enqueue(work)
    }
}