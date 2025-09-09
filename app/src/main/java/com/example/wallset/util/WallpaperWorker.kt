package com.example.wallset.util

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.wallset.util.SharedPrefManager.getSettings
import java.util.concurrent.TimeUnit

class WallpaperWorker(
    private val appContext: Context,
    private val params: WorkerParameters
) : CoroutineWorker(appContext = appContext,  params = params){

    override suspend fun doWork(): Result {

        val isHome = inputData.getBoolean(
            "isHome",
            defaultValue = true
        )

        val isLock = inputData.getBoolean(
            "isLock",
            defaultValue = true
        )

        val folderLocation = inputData.getString(
            "folderLocation",
        )
        Log.d("WallpaperWorker", "Strating WallpaperWorker")



        if (folderLocation.isNullOrEmpty()) return Result.failure()

        val images = listImagesInFolder(applicationContext, folderLocation.toUri())
        if (images.isEmpty()) return Result.failure()

        setWallpaper(applicationContext,images.random().uri, isHome = isHome, isLock = isLock,)

        return Result.success()
    }
}


suspend fun wallpaperWorkManager(
    context: Context,
){

    var settings: Settings = Settings(
        isHome = true,
        isLock = true,
        interval = .15F,
        folderLocation = ""
    )

        getSettings(context).collect { value ->
            settings = value
        }


    val data = workDataOf(
        "isHome" to settings.isHome,
        "isLock" to settings.isLock
    )
    val repeatInterval = 1

    val wallpaperWork = PeriodicWorkRequestBuilder<WallpaperWorker>(
        settings.interval.toLong(), TimeUnit.HOURS // change interval
    ).setInputData(data).build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "WallpaperChanger",
        ExistingPeriodicWorkPolicy.KEEP,
        wallpaperWork
    )


}