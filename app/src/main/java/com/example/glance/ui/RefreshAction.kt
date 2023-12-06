package com.example.glance.ui

import android.content.Context
import android.util.Log
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import com.example.glance.widget.MyAppWidget
import com.example.glance.widget.MyAppWidget.Companion.DESTINATION_KEY

class RefreshAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters,
    ) {
        Log.i(MyAppWidget.TAG, "refresh action ${parameters[DESTINATION_KEY]}")
    }
}