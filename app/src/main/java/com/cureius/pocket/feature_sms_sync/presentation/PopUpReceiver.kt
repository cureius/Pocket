package com.cureius.pocket.feature_sms_sync.presentation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log


class PopUpReceiver : BroadcastReceiver() {
    private val mService: PopUpService? = null
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_SHOW_FLOATING_WINDOW) {
            // Start the service to show the floating window
            Log.d("POP UP", "onReceive: received pop up request ")
            if (mService == null) {
                val serviceIntent = Intent(context, PopUpService::class.java)
                context.startService(serviceIntent)
            }
        } else if (intent.action == ACTION_HIDE_FLOATING_WINDOW) {
            // Stop the service to hide the floating window
            if (mService != null) {
                context.stopService(Intent(context, PopUpService::class.java))
            }
        }
    }

    companion object {
        private const val ACTION_SHOW_FLOATING_WINDOW = "com.example.ACTION_SHOW_FLOATING_WINDOW"
        private const val ACTION_HIDE_FLOATING_WINDOW = "com.example.ACTION_HIDE_FLOATING_WINDOW"
    }
}
