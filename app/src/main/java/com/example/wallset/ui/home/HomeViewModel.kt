package com.example.wallset.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wallset.util.Settings
import com.example.wallset.util.SharedPrefManager
import com.example.wallset.util.startWallpaperWorker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {

    private val _settings = MutableStateFlow<Settings?> (null)

    val settings: StateFlow<Settings?> = _settings

    fun saveSetting(context: Context, isHome: Boolean, isLock: Boolean, interval: Long, folderLocation: String){
        viewModelScope.launch {
            Log.d("HomeViewModel", "save setting ")
            SharedPrefManager.saveSettings(
                context = context,
                isHome = isHome,
                isLock = isLock,
                interval = interval,
                folderLocation = folderLocation
            )
        }
    }

    fun getSetting(context: Context,){
        viewModelScope.launch {
            SharedPrefManager.getSettings(context).collect { value ->
                _settings.value = value
            }
            Log.d("HomeViewModel", "settings : ${_settings.value}")
        }
    }

    fun startWallpaperWorkManager(context: Context){
        viewModelScope.launch {
            startWallpaperWorker(context)
        }
    }
}