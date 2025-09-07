package com.example.wallset.util

import android.content.Context
import androidx.core.content.edit

object SharedPrefManager {
    private const val PREFS_NAME = "prefs"
    private const val FOLDER_LOCATION = "folderLocation"



    fun saveFolderUri(context: Context, folderLocation: String) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit() { putString(FOLDER_LOCATION, folderLocation) }
    }

    fun getFolderUri(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return  sharedPreferences.getString(FOLDER_LOCATION, null)
    }

}
