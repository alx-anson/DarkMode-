package com.anson.darkmodeplus.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.anson.darkmodeplus.R
import com.anson.darkmodeplus.view.screens.Content
import com.anson.darkmodeplus.view.ui.DarkModePlusTheme
import com.anson.darkmodeplus.view.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkSystemAlertWindowPermission()
        viewModel.bindService(this)
        viewModel.updateMemories()
        setContent {
            DarkModePlusTheme {
                val snackBarHostState = remember { SnackbarHostState() }
                val memorySaved by viewModel.memorySaved.collectAsState()
                val scope = rememberCoroutineScope()
                if (memorySaved != 0) {
                    LaunchedEffect(Unit) {
                        scope.launch {
                            snackBarHostState.showSnackbar(
                                message = "Memoria $memorySaved guardada",
                                duration = SnackbarDuration.Short
                            )
                            delay(500)
                            viewModel.resetMemorySaved()
                        }
                    }
                }
                Scaffold(
                    snackbarHost = {
                        SnackbarHost(hostState = snackBarHostState)
                    }
                ) { contentPadding ->
                    Content(viewModel = viewModel, contentPadding)
                }
            }
        }
    }

    private fun checkSystemAlertWindowPermission() {
        if (!Settings.canDrawOverlays(this)) {
            showPermissionDialog()
        }
    }

    private fun showPermissionDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_title))
            .setMessage(R.string.dialog_message)
            .setPositiveButton(R.string.dialog_accept) { _, _ ->
                goToSettings()
            }
            .setNegativeButton(R.string.dialog_cancel, null)
            .show()
    }

    private fun goToSettings() {
        val intent =
            Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
        startActivity(intent)
    }

    override fun onDestroy() {
        viewModel.unbindService(this)
        super.onDestroy()
    }
}
