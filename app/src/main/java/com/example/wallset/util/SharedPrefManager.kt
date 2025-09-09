package com.example.wallset.util

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


data class Settings(
    val isHome: Boolean,
    val isLock: Boolean,
    val interval: Float,
    val folderLocation: String
    
)
object SharedPrefManager {

    val IS_HOME = booleanPreferencesKey("is_home")
    val IS_LOCK = booleanPreferencesKey("is_lock")
    val INTERVAL = floatPreferencesKey("interval")
    val FOLDER_LOCATION = stringPreferencesKey( "folderLocation")

    val Context.dataStore by preferencesDataStore(name = "wallpaper_settings")


    suspend fun saveSettings(context: Context, isHome: Boolean, isLock: Boolean, interval: Float, folderLocation: String) {
        context.dataStore.edit { prefs ->
            prefs[IS_HOME] = isHome
            prefs[IS_LOCK] = isLock
            prefs[INTERVAL] = interval
            prefs[FOLDER_LOCATION] = folderLocation
        }
    }

    fun getSettings(context: Context) : Flow<Settings>{
        
        val settingsFlow: Flow<Settings> =
            context.dataStore.data.map { prefs ->
                Settings(
                    isHome = prefs[IS_HOME] ?: false,
                    isLock = prefs[IS_LOCK] ?: false,
                    interval = prefs[INTERVAL] ?: 0.15F,
                    folderLocation = prefs[FOLDER_LOCATION] ?: ""
                )
            }

        return  settingsFlow
    }



}
