package com.example.wallset

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.wallset.ui.home.HomeScreen
import com.example.wallset.ui.theme.WallSetTheme
import com.example.wallset.util.WallpaperWorker
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WallSetTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current

                    HomeScreen()

                    val wallpaperWork = PeriodicWorkRequestBuilder<WallpaperWorker>(
                        15, TimeUnit.MINUTES // change interval
                    ).build()

                    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                        "WallpaperChanger",
                        ExistingPeriodicWorkPolicy.KEEP,
                        wallpaperWork
                    )

                    val workManager = WorkManager.getInstance(context)

                    workManager.getWorkInfosForUniqueWorkLiveData("WallpaperChanger")
                        .observe(this) { workInfos ->
                            if (!workInfos.isNullOrEmpty()) {
                                val state = workInfos[0].state
                                when (state) {
                                    WorkInfo.State.ENQUEUED -> Log.d("WM", "Work is scheduled")
                                    WorkInfo.State.RUNNING -> Log.d("WM", "Work is running")
                                    WorkInfo.State.SUCCEEDED -> Log.d("WM", "Work finished successfully")
                                    WorkInfo.State.FAILED -> Log.d("WM", "Work failed")
                                    WorkInfo.State.CANCELLED -> Log.d("WM", "Work was cancelled")
                                    else -> {}
                                }
                            }
                        }



                }
            }
        }
    }
}

