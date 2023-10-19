package com.anson.lowerbrightness

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.anson.lowerbrightness.databinding.ActivityMainBinding
import com.anson.lowerbrightness.Constants.Companion.overlayLv


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkPermission()
        setListeners()
    }

    private fun setListeners() {
        binding.btSwitch.setOnClickListener {
            if (binding.btSwitch.text == getText(R.string.on)) {
                binding.tvWarning.visibility = View.VISIBLE
                binding.btSwitch.text = getText(R.string.off)
                binding.btMinus.visibility = View.VISIBLE
                binding.btPlus.visibility = View.VISIBLE
                startService(
                    Intent(
                        this,
                        OverlayService::class.java
                    ).setAction(Constants.SHOW_OVERLAY)
                )
            } else {
                binding.tvWarning.visibility = View.GONE
                binding.btMinus.visibility = View.GONE
                binding.btPlus.visibility = View.GONE
                binding.btSwitch.text = getText(R.string.on)
                startService(
                    Intent(
                        this,
                        OverlayService::class.java
                    ).setAction(Constants.HIDE_OVERLAY)
                )
                overlayLv = 1
            }
        }
        binding.btPlus.setOnClickListener {
            if (overlayLv <= 0) return@setOnClickListener
            val intent = Intent(this, OverlayService::class.java)
            intent.putExtra("overlay_lv", overlayLv--)
            intent.action = Constants.SHOW_OVERLAY
            startService(Intent(intent))
        }
        binding.btMinus.setOnClickListener {
            if (overlayLv >= 3) return@setOnClickListener
            val intent = Intent(this, OverlayService::class.java)
            intent.putExtra("overlay_lv", overlayLv++)
            intent.action = Constants.SHOW_OVERLAY
            startService(Intent(intent))
        }
    }

    private fun checkPermission() {
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
                checkPermission()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        startService(Intent(this, OverlayService::class.java).setAction("HIDE_OVERLAY"))
    }


}