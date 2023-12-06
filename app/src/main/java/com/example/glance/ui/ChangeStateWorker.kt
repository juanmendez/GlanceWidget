package com.example.glance.ui

import android.content.Context
import android.util.Log
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.glance.widget.MyAppWidget

class ChangeStateWorker(
    val context: Context,
    val params: WorkerParameters,
) : CoroutineWorker(context, params) {
    companion object {
        const val TAG = "ChangeStateWorker"
    }

    override suspend fun doWork(): Result {
        Log.i(TAG, "doWork")

        Repository.count.value = Repository.count.value + 1

        // Fetch data or do some work and then update all instance of your widget
        MyAppWidget().updateAll(context)
        return Result.success()
    }
}