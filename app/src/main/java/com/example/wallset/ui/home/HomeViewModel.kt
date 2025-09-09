package com.example.wallset.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wallset.util.Settings
import com.example.wallset.util.SharedPrefManager
import com.example.wallset.util.wallpaperWorkManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {

    private val _settings = MutableStateFlow<Settings> (Settings(
        isHome = true,
        isLock = true,
        interval = 0.15F,
        folderLocation = ""
    ))

    val settings: StateFlow<Settings> = _settings

    fun saveSetting(context: Context, isHome: Boolean, isLock: Boolean, interval: Float, folderLocation: String){
        viewModelScope.launch {
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
        }
    }

    fun startWallpaperWorkManager(context: Context){
        viewModelScope.launch {
            wallpaperWorkManager(context)
        }
    }
}