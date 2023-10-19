package com.anson.lowerbrightness

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.anson.lowerbrightness.Constants.Companion.overlayLv

class OverlayService : Service() {
    private var overlayView: View? = null
    private var isOverlayVisible = false
    private var overlayLayers =
        arrayOf(R.color.overlay_25, R.color.overlay_50, R.color.overlay_75, R.color.overlay_100)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            if (isOverlayVisible) {
                hideOverlay()
            }

            if (intent.action == Constants.SHOW_OVERLAY) {
                showOverlay(overlayLv)
            } else if (intent.action == Constants.HIDE_OVERLAY) {
                hideOverlay()
            }
        }
        return START_STICKY
    }

    private fun showOverlay(overlayLv: Int) {
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        overlayView = View(this)
        overlayView?.setBackgroundColor(ContextCompat.getColor(this, overlayLayers[overlayLv]))
        windowManager.addView(overlayView, params)
        isOverlayVisible = true
    }

    private fun hideOverlay() {
        val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        windowManager.removeView(overlayView)
        isOverlayVisible = false
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}