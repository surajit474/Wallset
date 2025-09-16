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
suspend fun startWallpaperWorker(context: Context) {
    Log.d("WallpaperWorker", "Starting WorkManager setup")

    val settings = SharedPrefManager.getSettings(context).first()

    val data = workDataOf(
        "isHome" to settings.isHome,
        "isLock" to settings.isLock,
        "folderLocation" to settings.folderLocation
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

























//import android.content.Context
//import android.util.Log
//import androidx.core.net.toUri
//import androidx.work.CoroutineWorker
//import androidx.work.ExistingPeriodicWorkPolicy
//import androidx.work.PeriodicWorkRequestBuilder
//import androidx.work.WorkManager
//import androidx.work.WorkerParameters
//import androidx.work.workDataOf
//import com.example.wallset.util.SharedPrefManager.getSettings
//import java.util.concurrent.TimeUnit
//
//class WallpaperWorker(
//    private val appContext: Context,
//    private val params: WorkerParameters
//) : CoroutineWorker(appContext = appContext,  params = params){
//
//    override suspend fun doWork(): Result {
//
//        val isHome = inputData.getBoolean(
//            "isHome",
//            defaultValue = true
//        )
//
//        val isLock = inputData.getBoolean(
//            "isLock",
//            defaultValue = true
//        )
//
//        val folderLocation = inputData.getString(
//            "folderLocation",
//        )
//        Log.d("WallpaperWorker", "Strating WallpaperWorker")
//
//
//
//        if (folderLocation.isNullOrEmpty()) return Result.failure()
//
//        val images = listImagesInFolder(applicationContext, folderLocation.toUri())
//        if (images.isEmpty()) return Result.failure()
//
//        setWallpaper(applicationContext,images.random().uri, isHome = isHome, isLock = isLock,)
//
//        return Result.success()
//    }
//}
//
//
//suspend fun wallpaperWorkManager(
//    context: Context,
//){
//
//    Log.d("wallpaperWorkManager", " Entering wallpaperWorkManager")
//    var settings: Settings = Settings(
//        isHome = false,
//        isLock = false,
//        interval = 1,
//        folderLocation = ""
//    )
//
//    getSettings(context).collect { value ->
//            settings = value
//        }
//
//
//    val data = workDataOf(
//        "isHome" to settings.isHome,
//        "isLock" to settings.isLock,
//        "folderLocation" to settings.folderLocation
//    )
//
//
//    val wallpaperWork = PeriodicWorkRequestBuilder<WallpaperWorker>(
//        settings.interval, TimeUnit.HOURS // change interval
//    ).setInputData(data).build()
//
//    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
//        "WallpaperChanger",
//        ExistingPeriodicWorkPolicy.KEEP,
//        wallpaperWork
//    )
//
//    val workInfos by workManager
//        .getWorkInfosForUniqueWorkLiveData("WallpaperChanger")
//        .observeAsState()
//
//    workInfos?.firstOrNull()?.let { info ->
//        when (info.state) {
//            WorkInfo.State.RUNNING -> Text("Work is running…")
//            WorkInfo.State.ENQUEUED -> Text("Work scheduled")
//            WorkInfo.State.SUCCEEDED -> Text("Work finished")
//            else -> {}
//        }
//    }
//
//
//}