package com.example.glance.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.glance.ui.theme.GlanceWidgetTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GlanceWidgetTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Greeting("Android") {
                        val data = Data.Builder()
                            .build()

                        val work = OneTimeWorkRequest.Builder(ChangeStateWorker::class.java)
                            .setInputData(data)
                            .build()

                        WorkManager.getInstance(this).enqueue(work)
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier, updateWidgets: () -> Unit = {}) {
    val count = remember { Repository.count}

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Repository count: ${Repository.count}",
            modifier = modifier
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                updateWidgets()
            },
        ) {
            Text("Update repository count, and refresh widgets")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GlanceWidgetTheme {
        Greeting("Android")
    }
}