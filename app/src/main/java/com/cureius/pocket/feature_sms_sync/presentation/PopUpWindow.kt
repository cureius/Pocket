package com.cureius.pocket.feature_sms_sync.presentation

import android.os.Build
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PopUpWindow() {
    val context = LocalContext.current
    val windowManager = context.getSystemService(WindowManager::class.java)
    val layoutParams = WindowManager.LayoutParams()

    // Set window type to overlay on other apps
    layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY

    // Set window flags to enable focus and touch events
    layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH

    // Set window dimensions and position
    layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT
    layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
    layoutParams.x = 0
    layoutParams.y = 0

    AndroidView(modifier = Modifier.fillMaxSize(), factory = { context ->
        val textView = TextView(context)
        textView.text = "Hello, World!"
        textView.setBackgroundColor(Color.Red.toArgb())
        textView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        textView
    }, update = {
        windowManager.addView(it, layoutParams)
    })
}
