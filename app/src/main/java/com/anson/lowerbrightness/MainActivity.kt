package com.anson.lowerbrightness

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.anson.lowerbrightness.Constants.Companion.HIDE_OVERLAY
import com.anson.lowerbrightness.Constants.Companion.overlayLv
import com.anson.lowerbrightness.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val delay = 200L
    private var lastButtonClickTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkSystemAlertWindowPermission()
        setListeners()
    }

    private fun setListeners() {
        binding.btSwitch.setOnClickListener {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastButtonClickTime >= delay) {
                lastButtonClickTime = currentTime
                doClick()
            }
        }
        binding.btPlus.setOnClickListener {
            if (overlayLv <= 0) return@setOnClickListener
            startServiceOverlay(overlayLv--)

        }
        binding.btMinus.setOnClickListener {
            if (overlayLv >= 3) return@setOnClickListener
            startServiceOverlay(overlayLv++)
        }
    }

    private fun doClick() {
        if (binding.btSwitch.text == getText(R.string.on)) {
            clickOn()
        } else {
            clickOff()
        }
    }

    private fun clickOn() {
        binding.btSwitch.text = getText(R.string.off)
        binding.tvWarning.visibility = View.VISIBLE
        binding.btMinus.visibility = View.VISIBLE
        binding.btPlus.visibility = View.VISIBLE
        startService(
            Intent(
                this,
                OverlayService::class.java
            ).setAction(Constants.SHOW_OVERLAY)
        )
    }

    private fun clickOff() {
        binding.btSwitch.text = getText(R.string.on)
        binding.tvWarning.visibility = View.GONE
        binding.btMinus.visibility = View.GONE
        binding.btPlus.visibility = View.GONE
        overlayLv = 1
        startService(
            Intent(
                this,
                OverlayService::class.java
            ).setAction(HIDE_OVERLAY)
        )
    }

    private fun startServiceOverlay(overlayLv: Int) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastButtonClickTime >= delay) {
            lastButtonClickTime = currentTime
            val intent = Intent(this, OverlayService::class.java)
            intent.putExtra("overlay_lv", overlayLv)
            intent.action = Constants.SHOW_OVERLAY
            startService(Intent(intent))
        }
    }

    private fun goToSettings() {
        if (!Settings.canDrawOverlays(this)) {
            val intent =
                Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
            startActivityForResult(intent, Constants.OVERLAY_PERMISSION_REQUEST_CODE)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.OVERLAY_PERMISSION_REQUEST_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                goToSettings()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        startService(Intent(this, OverlayService::class.java).setAction(HIDE_OVERLAY))
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
}