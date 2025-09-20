package com.example.wallset.util

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit


class WallpaperWorker(
    private val appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val isHome = inputData.getBoolean("isHome", true)
        val isLock = inputData.getBoolean("isLock", true)
        val folderLocation = inputData.getString("folderLocation")

        Log.d("WallpaperWorker", "Starting WallpaperWorker")

        if (folderLocation.isNullOrEmpty()) return Result.failure()

        val images = listImagesInFolder(applicationContext, folderLocation.toUri())
        if (images.isEmpty()) return Result.failure()

        setWallpaper(applicationContext, images.random().uri, isHome, isLock)
        return Result.success()
    }
}

/**
 * Starts the wallpaper worker with current settings.
 */
suspend fun startWallpaperWorker(context: Context,) {
    Log.d("WallpaperWorker", "Starting WorkManager setup")
    var folderLocation = ""
    val settings = SharedPrefManager.getSettings(context).first()

    if (isSystemDarkTheme(context)){
        folderLocation = settings.darkFolderLocation
    }else{
        folderLocation = settings.lightFolderLocation
    }


    val data = workDataOf(
        "isHome" to settings.isHome,
        "isLock" to settings.isLock,
        "folderLocation" to folderLocation
    )

    val wallpaperWork = PeriodicWorkRequestBuilder<WallpaperWorker>(
        settings.interval.toLong(), TimeUnit.HOURS
    ).setInputData(data).build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "WallpaperChanger",
        ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
        wallpaperWork
    )
}



fun isSystemDarkTheme(context: Context): Boolean {
    val currentNightMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    return when (currentNightMode) {
        Configuration.UI_MODE_NIGHT_YES -> true   // Dark
        Configuration.UI_MODE_NIGHT_NO -> false  // Light
        else -> false // Default fallback
    }
}
