package com.cureius.pocket.feature_sms_sync.presentation

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.cureius.pocket.R

class PopUpService : Service() {
    private var mWindowManager: WindowManager? = null
    private var mFloatingView: View? = null

    override fun onCreate() {
        super.onCreate()
        Log.d("POP up", "onCreate: adding view")

        // Create the floating view
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.pop_up_window, null)

        // Get a reference to the TextView element
        val floatingText = mFloatingView!!.findViewById<TextView>(R.id.floating_text)

        // Set a click listener on the TextView element
        floatingText.setOnClickListener {
            // Change the text of the TextView element when it is clicked
            floatingText.text = "You clicked the floating window!"
        }

        // Add the view to the WindowManager
        mWindowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        val params = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
        } else {
            null
        }
        mWindowManager!!.addView(mFloatingView, params)
        Log.d("POP up", "onCreate: added view")
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()

        // Remove the view from the WindowManager
        mWindowManager?.removeView(mFloatingView)
    }
}
