package com.anson.darkmodeplus.view.viewmodel

import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anson.darkmodeplus.data.DataStoreManager
import com.anson.darkmodeplus.view.activity.OverlayService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    private var overlayService: OverlayService? = null
    private var isBound = false

    private val _overlayEnabled = MutableStateFlow(false)
    val overlayEnabled: StateFlow<Boolean> = _overlayEnabled

    private val _overlayLv = MutableStateFlow(115)
    val overlayLv: StateFlow<Int> = _overlayLv

    private val _memorySaved = MutableStateFlow(0)
    val memorySaved: StateFlow<Int> = _memorySaved

    private var memo1 = 0
    private var memo2 = 0
    private var memo3 = 0

    fun updateMemories() {
        viewModelScope.launch {
            dataStoreManager.getMemory(1).collect { value ->
                value?.let {
                    memo1 = it.toInt()
                }
            }
        }
        viewModelScope.launch {
            dataStoreManager.getMemory(2).collect { value ->
                value?.let {
                    memo2 = it.toInt()
                }
            }
        }
        viewModelScope.launch {
            dataStoreManager.getMemory(3).collect { value ->
                value?.let {
                    memo3 = it.toInt()
                }
            }
        }
    }


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

    fun selectMemory(memory: Int) {
        _overlayLv.value = when (memory) {
            1 -> if (memo1 != 0) memo1 else return
            2 -> if (memo2 != 0) memo2 else return
            3 -> if (memo3 != 0) memo3 else return
            else -> 100
        }
        updateOverlayLevel(overlayLv.value)
    }

    fun saveMemory(memory: Int) {
        viewModelScope.launch {
            dataStoreManager.saveMemory(overlayLv.value.toString(), memory)
            updateMemories()
            _memorySaved.value = memory
        }
    }

    fun resetMemorySaved() {
        _memorySaved.value = 0
    }
}
