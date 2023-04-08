package com.cureius.pocket.feature_sms_sync.domain

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Telephony
import android.telephony.SmsMessage
import android.util.Log
import androidx.annotation.RequiresApi
import com.cureius.pocket.feature_sms_sync.util.SyncUtils
import com.cureius.pocket.feature_transaction.domain.use_case.TransactionUseCases
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SmsReceiver : BroadcastReceiver() {
    @Inject
    lateinit var transactionUseCases: TransactionUseCases
    private val scope = CoroutineScope(Dispatchers.IO)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        val tag = "SMS Receiver"
        // Use the ViewModel instance here

        // initialize the addTransactionViewModel property
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val bundle = intent.extras
            if (bundle != null) {
                val pdus = bundle.get("pdus") as Array<*>
                for (i in pdus.indices) {
                    val message = SmsMessage.createFromPdu(pdus[i] as ByteArray)
                    val address = message.originatingAddress
                    val body = message.messageBody
                    val date = message.timestampMillis
                    if ((body.lowercase().contains("a/c") || body.lowercase()
                            .contains("card")) && (body.contains("credited") || body.contains("debited"))
                    ) {
                        if (address != null) {
                            SyncUtils.extractTransactionalDetails(
                                date, address, body
                            ).let {
                                Log.d(tag, "onReceive: $it")
                                scope.launch {
                                    transactionUseCases.addTransaction(it)
                                }
                            }
                        }
                        // Prevent other apps from receiving the broadcast
                        abortBroadcast()
                    }
                    Log.d(tag, "onReceive: $address -- $body")
                    // Do something with the message and sender
                }
            }
        }
    }
}
