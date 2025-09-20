package com.example.wallset.util

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


data class Settings(
    val isHome: Boolean,
    val isLock: Boolean,
    val interval: Long,
    val darkFolderLocation: String,
    val lightFolderLocation: String

)
object SharedPrefManager {

    val IS_HOME = booleanPreferencesKey("is_home")
    val IS_LOCK = booleanPreferencesKey("is_lock")
    val INTERVAL = longPreferencesKey("interval")
    val DARK_FOLDER_LOCATION = stringPreferencesKey( "darkFolderLocation")
    val LIGHT_FOLDER_LOCATION = stringPreferencesKey( "lightFolderLocation")

    val Context.dataStore by preferencesDataStore(name = "wallpaper_settings")


    suspend fun saveSettings(context: Context, isHome: Boolean, isLock: Boolean, interval: Long, darkFolderLocation: String, lightFolderLocation: String) {
        Log.d("SharedPrefManager","saveSettings"  )
        context.dataStore.edit { prefs ->
            prefs[IS_HOME] = isHome
            prefs[IS_LOCK] = isLock
            prefs[INTERVAL] = interval
            prefs[DARK_FOLDER_LOCATION] = darkFolderLocation
            prefs[LIGHT_FOLDER_LOCATION] = lightFolderLocation
        }
    }

    fun getSettings(context: Context) : Flow<Settings>{
        Log.d("SharedPrefManager","getSettings"  )

        val settingsFlow: Flow<Settings> =
            context.dataStore.data.map { prefs ->
                Settings(
                    isHome = prefs[IS_HOME] ?: true,
                    isLock = prefs[IS_LOCK] ?: true,
                    interval = prefs[INTERVAL] ?: 1L,
                    darkFolderLocation = prefs[DARK_FOLDER_LOCATION] ?: "",
                    lightFolderLocation = prefs[LIGHT_FOLDER_LOCATION] ?: ""
                )
            }

        return  settingsFlow
    }



}
