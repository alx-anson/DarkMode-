package com.anson.darkmodeplus.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.anson.darkmodeplus.R
import com.anson.darkmodeplus.view.screens.Content
import com.anson.darkmodeplus.view.ui.DarkModePlusTheme
import com.anson.darkmodeplus.view.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkSystemAlertWindowPermission()
        viewModel.bindService(this)
        setContent {
            DarkModePlusTheme {
                Content(viewModel = viewModel)
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
