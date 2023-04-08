package com.cureius.pocket.feature_sms_sync.domain

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder

class SmsService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        // Register your SMS receiver here
        val smsReceiver = SmsReceiver()
        val intentFilter = IntentFilter("android.provider.Telephony.SMS_RECEIVED")
        registerReceiver(smsReceiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregister your SMS receiver here
        val smsReceiver = SmsReceiver()
        unregisterReceiver(smsReceiver)
    }
}
