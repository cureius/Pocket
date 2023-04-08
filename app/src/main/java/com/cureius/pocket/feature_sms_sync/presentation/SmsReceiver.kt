package com.cureius.pocket.feature_sms_sync.presentation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Telephony
import android.telephony.SmsMessage
import android.util.Log
import androidx.annotation.RequiresApi
import com.cureius.pocket.feature_sms_sync.presentation.PopUpService
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
    private val mService: PopUpService? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        val tag = "SMS Receiver"
        // Use the ViewModel instance here

        // initialize the addTransactionViewModel property
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val bundle = intent.extras

            val showIntent = Intent("com.example.ACTION_SHOW_FLOATING_WINDOW")
            val hideIntent = Intent("com.example.ACTION_HIDE_FLOATING_WINDOW")
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
//                                if (mService == null) {
                                    Log.d(tag, "onReceive: triggering")
                                    val serviceIntent = Intent(context, PopUpService::class.java)
                                    serviceIntent.putExtra("detected-transaction-date", date)
                                    serviceIntent.putExtra("detected-transaction-address", address)
                                    serviceIntent.putExtra("detected-transaction-body", body)
                                    context.startService(serviceIntent)
//                                }
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
