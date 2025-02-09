package com.anson.darkmodeplus.view.activity

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Binder
import android.os.IBinder
import android.view.View
import android.view.WindowManager

class OverlayService : Service() {
    private val binder = OverlayBinder()
    private var overlayView: View? = null
    private var windowManager: WindowManager? = null
    private var isOverlayVisible = false
    private var overlayLv: Int = 100

    inner class OverlayBinder : Binder() {
        fun getService(): OverlayService = this@OverlayService
    }

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    fun setOverlayVisibility(visible: Boolean) {
        if (visible) showOverlay() else hideOverlay()
    }

    fun setOverlayOpacity(level: Int) {
        overlayLv = level
        overlayView?.setBackgroundColor(getOverlayColor(level))
    }

    private fun showOverlay() {
        if (isOverlayVisible) return

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        overlayView = View(this).apply {
            setBackgroundColor(getOverlayColor(overlayLv))
        }

        windowManager?.addView(overlayView, params)
        isOverlayVisible = true
    }

    private fun hideOverlay() {
        overlayView?.let {
            windowManager?.removeView(it)
            overlayView = null
        }
        isOverlayVisible = false
    }

    private fun getOverlayColor(alpha: Int): Int {
        val clampedAlpha = alpha.coerceIn(0, 240)
        return (clampedAlpha shl 24) or 0x000000
    }

    override fun onDestroy() {
        hideOverlay()
        super.onDestroy()
    }
}
