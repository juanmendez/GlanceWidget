package com.example.glance.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalSize
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.action.actionStartService
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.material3.ColorProviders
import androidx.glance.text.Text
import com.example.glance.R
import com.example.glance.SyncService
import com.example.glance.ui.MainActivity
import com.example.glance.ui.RefreshAction
import com.example.glance.ui.Repository
import com.example.glance.ui.Repository.ERROR
import com.example.glance.ui.Repository.LOADING
import com.example.glance.ui.Repository.SUCCESS
import com.example.glance.ui.theme.DarkColorScheme
import com.example.glance.ui.theme.LightColorScheme
import kotlinx.coroutines.launch

class MyAppWidget : GlanceAppWidget() {
    companion object {
        const val TAG = "MyAppWidget"
        val DESTINATION_KEY = ActionParameters.Key<String>("Hello")

        private val SMALL_SQUARE = DpSize(150.dp, 100.dp)
        private val MEDIUM_RECTANGLE = DpSize(200.dp, 100.dp)
        private val BIG_SQUARE = DpSize(250.dp, 250.dp)
    }

    override val sizeMode = SizeMode.Responsive(
        setOf(
            SMALL_SQUARE,
            MEDIUM_RECTANGLE,
            BIG_SQUARE
        )
    )

    override suspend fun provideGlance(context: Context, id: GlanceId) {

        // In this method, load data needed to render the AppWidget.
        // Use `withContext` to switch to another thread for long running
        // operations.

        provideContent {
            // create your AppWidget here
            GlanceTheme(
                colors = ColorProviders(
                    light = LightColorScheme,
                    dark = DarkColorScheme
                ),
            ) {
                MyContent()
            }

        }
    }

    @Composable
    private fun MyContent() {
        val repository = remember { Repository }
        val state by repository.getStates().collectAsState(LOADING)

        // Size will be one of the sizes defined above.
        val size = LocalSize.current

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
                    if (size.width >= BIG_SQUARE.width) {
                        BigContent(repository)
                    } else if (size.width >= MEDIUM_RECTANGLE.width) {
                        MediumContent(repository)
                    } else {
                        SmallContent(repository)
                    }
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
    private fun SmallContent(repository: Repository) {
        val count by remember { repository.count }

        val scope = rememberCoroutineScope()

        // Make column to be scrollable
        LazyColumn(
            modifier = GlanceModifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Button(text = "refresh, count ${count}", onClick = {
                    scope.launch {
                        count
                    }
                })
            }
        }
    }

    @Composable
    private fun MediumContent(repository: Repository) {
        val count by remember { repository.count }

        val scope = rememberCoroutineScope()

        // Make column to be scrollable
        LazyColumn(
            modifier = GlanceModifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {
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
            }

            item {
                Button(text = "refresh, count ${count}", onClick = {
                    scope.launch {
                        count
                    }
                })
            }
        }
    }

    @Composable
    private fun BigContent(repository: Repository) {
        val count by remember { repository.count }

        val scope = rememberCoroutineScope()

        // Make column to be scrollable
        LazyColumn(
            modifier = GlanceModifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Image(
                    provider = ImageProvider(R.drawable.ic_launcher_foreground),
                    modifier = GlanceModifier.clickable(
                        onClick = actionRunCallback<RefreshAction>(
                            parameters = actionParametersOf(DESTINATION_KEY to "go")
                        )
                    ),
                    contentDescription = "Refresh"
                )
            }

            item {
                Text(text = "Where to?", modifier = GlanceModifier.padding(12.dp))
            }

            item {
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
            }

            item {
                Text(
                    text = "counting ${count}",
                    modifier = GlanceModifier.padding(30.dp)
                )

            }

            item {
                Button(text = "refresh", onClick = {
                    scope.launch {
                        count
                    }
                })
            }
        }
    }
}

