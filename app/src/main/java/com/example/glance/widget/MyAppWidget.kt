package com.example.glance.widget

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.action.actionStartService
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.Text
import com.example.glance.R
import com.example.glance.SyncService
import com.example.glance.ui.MainActivity
import com.example.glance.ui.RefreshAction
import com.example.glance.ui.Repository
import com.example.glance.ui.Repository.ERROR
import com.example.glance.ui.Repository.LOADING
import com.example.glance.ui.Repository.SUCCESS

class MyAppWidget : GlanceAppWidget() {
    companion object {
        const val TAG = "MyAppWidget"
        val DESTINATION_KEY = ActionParameters.Key<String>("Hello")
    }

    override suspend fun provideGlance(context: Context, id: GlanceId) {

        // In this method, load data needed to render the AppWidget.
        // Use `withContext` to switch to another thread for long running
        // operations.

        provideContent {
            // create your AppWidget here
            MyContent()
        }
    }

    @Composable
    private fun MyContent() {
        val repository = remember { Repository }
        val states = remember { repository.getStates() }

        val state by states.collectAsState(LOADING)

        Box(modifier = GlanceModifier.background(R.color.teal_200)) {
            when (state) {
                LOADING -> {
                    Column(
                        modifier = GlanceModifier.fillMaxSize(),
                        verticalAlignment = Alignment.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("LOADING")
                    }
                }

                SUCCESS -> {
                    SuccessContent(repository)
                }

                ERROR -> {
                    Image(
                        provider = ImageProvider(android.R.drawable.stat_notify_error),
                        contentDescription = "Loading"
                    )
                }

            }
        }

    }

    @Composable
    private fun SuccessContent(repository: Repository) {
        var count by remember { mutableStateOf(repository.count) }

        Column(
            modifier = GlanceModifier.fillMaxSize(),
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                provider = ImageProvider(R.drawable.ic_launcher_foreground),
                modifier = GlanceModifier.clickable(
                    onClick = actionRunCallback<RefreshAction>(
                        parameters = actionParametersOf(DESTINATION_KEY to "go")
                    )
                ),
                contentDescription = "Refresh"
            )

            Text(text = "Where to?", modifier = GlanceModifier.padding(12.dp))
            Row(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    text = "Go Home",
                    onClick = actionStartActivity<MainActivity>()
                )
                Button(
                    text = "Work",
                    onClick = actionStartActivity<MainActivity>()
                )
                Button(
                    text = "Sync",
                    onClick = actionStartService<SyncService>(isForegroundService = true)
                )
            }

            Text(
                text = "counting ${count}",
                modifier = GlanceModifier.padding(30.dp)
            )

            Button(text = "Refresh", onClick = {
                count = repository.count
            })
        }
    }
}

