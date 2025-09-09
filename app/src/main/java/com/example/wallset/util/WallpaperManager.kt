package com.example.wallset.util

import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.documentfile.provider.DocumentFile

@Composable
fun FolderPicker(
    onFolderPicked: (Uri) -> Unit,
    onFolder: (Boolean) -> Unit = {},
) {
    val context = LocalContext.current
    val initialUri: Uri? = null

    val openTreeLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { treeUri: Uri? ->
        if (treeUri != null) {

            try {
                context.contentResolver.takePersistableUriPermission(
                    treeUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
            } catch (se: SecurityException) {
                // The provider might not allow persistence, or flags not granted
            }
            onFolderPicked(treeUri)
            onFolder(false)
        }
    }
    LaunchedEffect(Unit) {
        openTreeLauncher.launch(initialUri)
    }


}



fun listImagesInFolder(context: Context, treeUri: Uri): List<DocumentFile> {
    val tree = DocumentFile.fromTreeUri(context, treeUri) ?: return emptyList()
    val imageFiles = tree.listFiles().toList().filter { it.type?.startsWith("image/") == true }

    return imageFiles
}


fun setWallpaper(context: Context, uri: Uri, isHome: Boolean , isLock: Boolean ) {
        try {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            val bitmap = ImageDecoder.decodeBitmap(source)

            val wallpaperManager = WallpaperManager.getInstance(context)
            if (isHome) {
                wallpaperManager.setBitmap(
                    bitmap,
                    null,
                    true,
                    WallpaperManager.FLAG_SYSTEM
                )  // ✅ Sets home screen wallpaper
            }
            if (isLock) {
                wallpaperManager.setBitmap(
                    bitmap,
                    null,
                    true,
                    WallpaperManager.FLAG_LOCK
                ) // For lock screen
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

}



