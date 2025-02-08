package com.anson.darkmodeplus

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkSystemAlertWindowPermission()
        setContent {
            DarkModePlusTheme {
                Content()
            }
        }
    }

    private fun checkSystemAlertWindowPermission() {
        if (!hasSystemAlertWindowPermission()) {
            showPermissionDialog()
        }
    }

    private fun hasSystemAlertWindowPermission(): Boolean {
        return Settings.canDrawOverlays(this)
    }

    private fun showPermissionDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_title))
            .setMessage(R.string.dialog_message)
            .setPositiveButton(R.string.dialog_accept) { _, _ ->
                goToSettings()
            }
            .setNegativeButton(R.string.dialog_cancel) { _, _ ->
                checkSystemAlertWindowPermission()
            }
            .show()
    }

    private fun goToSettings() {
        if (!Settings.canDrawOverlays(this)) {
            val intent =
                Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
            startActivityForResult(intent, Constants.OVERLAY_PERMISSION_REQUEST_CODE)
        }
    }
}

@Composable
fun Content() {
    var buttonPushed by remember { mutableStateOf(false) }
    val buttonText = if (buttonPushed) "OFF" else "ON"
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color().background), contentAlignment = Alignment.Center
    ) {
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Pulsa el botón para oscurecer la pantalla",
                fontSize = 32.sp,
                color = Color().onBackground,
                style = MaterialTheme.typo.bodyLarge
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(modifier = Modifier
                .height(80.dp)
                .width(230.dp),
                enabled = true,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color().primary,
                    contentColor = Color().onPrimary
                ),
                onClick = { buttonPushed = !buttonPushed },
                content = {
                    Text(
                        text = buttonText,
                        fontSize = 32.sp,
                        color = Color().onBackground,
                        style = MaterialTheme.typo.bodyLarge
                    )
                }
            )
        }
    }
}


