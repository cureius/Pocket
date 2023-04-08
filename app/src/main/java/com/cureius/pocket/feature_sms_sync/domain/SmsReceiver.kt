package com.cureius.pocket.feature_sms_sync.domain

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.util.Log

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val tag = "SMS Receiver"
        if (intent.action == "android.provider.Telephony.SMS_RECEIVED") {
            val bundle = intent.extras
            if (bundle != null) {
                Log.d(tag, "onReceive: ${bundle.toString()}")
                val pdus = bundle.get("pdus") as Array<*>
                for (i in pdus.indices) {
                    val message = SmsMessage.createFromPdu(pdus[i] as ByteArray)
                    val sender = message.originatingAddress
                    val messageBody = message.messageBody
                    Log.d(tag, "onReceive: $sender")
                    Log.d(tag, "onReceive: $messageBody")
                    // Do something with the message and sender
                }
            }
        }
    }
}
