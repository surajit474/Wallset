package com.example.wallset.util

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.wallset.util.SharedPrefManager.getFolderUri

class WallpaperWorker(
    private val appContext: Context,
    private val params: WorkerParameters
) : CoroutineWorker(appContext = appContext,  params = params){

    override suspend fun doWork(): Result {
        Log.d("WallpaperWorker", "Strating WallpaperWorker")
        val folderUri = getFolderUri(applicationContext) ?: return Result.failure()
        val images = listImagesInFolder(applicationContext, folderUri.toUri())
        if (images.isEmpty()) return Result.failure()

        setWallpaper(applicationContext,images.random().uri)

        return Result.success()
    }
}