package com.anson.darkmodeplus.view.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.AndroidViewModel
import com.anson.darkmodeplus.view.activity.OverlayService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private var overlayService: OverlayService? = null
    private var isBound = false

    private val _overlayEnabled = MutableStateFlow(false)
    val overlayEnabled: StateFlow<Boolean> = _overlayEnabled

    private val _overlayLv = MutableStateFlow(100)
    val overlayLv: StateFlow<Int> = _overlayLv

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: android.content.ComponentName?, service: IBinder?) {
            val binder = service as? OverlayService.OverlayBinder
            overlayService = binder?.getService()
            isBound = true
        }

        override fun onServiceDisconnected(name: android.content.ComponentName?) {
            overlayService = null
            isBound = false
        }
    }

    fun bindService(context: Context) {
        Intent(context, OverlayService::class.java).also { intent ->
            context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    fun unbindService(context: Context) {
        if (isBound) {
            context.unbindService(serviceConnection)
            isBound = false
        }
    }

    fun toggleOverlay() {
        _overlayEnabled.value = !_overlayEnabled.value
        overlayService?.setOverlayVisibility(_overlayEnabled.value)
    }

    fun updateOverlayLevel(level: Int) {
        _overlayLv.value = level
        overlayService?.setOverlayOpacity(level)
    }
}
