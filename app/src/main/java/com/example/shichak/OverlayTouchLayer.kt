package com.example.shichak

import android.content.Context
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager

class OverlayTouchLayer(
    context: Context,
    private val onTouch: (MotionEvent) -> Boolean
) : View(context) {

    private val windowManager =
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    private var attached = false

    init {
        setBackgroundColor(android.graphics.Color.TRANSPARENT)
        isClickable = true
        isFocusable = false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return onTouch(event)
    }

    fun show(x: Int, y: Int, width: Int, height: Int) {
        if (attached) return

        val params = WindowManager.LayoutParams(
            width,
            height,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            this.x = x
            this.y = y
        }

        windowManager.addView(this, params)
        attached = true
    }

    fun move(x: Int, y: Int, width: Int, height: Int) {
        if (!attached) return

        val params = layoutParams as WindowManager.LayoutParams
        params.x = x
        params.y = y
        params.width = width
        params.height = height

        windowManager.updateViewLayout(this, params)
    }

    fun remove() {
        if (!attached) return

        try {
            windowManager.removeView(this)
        } catch (_: Exception) {
        }

        attached = false
    }
}
