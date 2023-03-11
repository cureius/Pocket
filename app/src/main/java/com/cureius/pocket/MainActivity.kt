package com.cureius.pocket

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.filled.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.cureius.pocket.feature_navigation.presentation.NavigationController
import com.cureius.pocket.feature_sms_sync.util.SyncUtils
import com.cureius.pocket.feature_transaction.domain.model.Transaction
import com.cureius.pocket.feature_transaction.presentation.add_transaction.AddTransactionEvent
import com.cureius.pocket.feature_transaction.presentation.add_transaction.AddTransactionViewModel
import com.cureius.pocket.ui.theme.PocketTheme
import com.cureius.pocket.util.SharedPreferencesUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val tag: String = "MainActivity"
    private val PERMISSION_REQUEST_CODE = 1

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_SMS),
                PERMISSION_REQUEST_CODE
            )
        } else {
            initialSmsSync(this)
        }

        setContent {
            PocketTheme {
                NavigationController(navController = rememberNavController())
            }
        }
    }

    @Deprecated("Deprecated in Java")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initialSmsSync(this)
            } else {
                Toast.makeText(
                    this,
                    "Permission Denied!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun readSMS(): List<Transaction> {
        val transactionList = mutableListOf<Transaction>()
        val cursor = contentResolver.query(
            android.provider.Telephony.Sms.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        if (cursor?.moveToFirst() == true) {
            do {
                val body = cursor.getString(cursor.getColumnIndexOrThrow("body"))
                val address = cursor.getString(cursor.getColumnIndexOrThrow("address"))
                val date = cursor.getLong(cursor.getColumnIndexOrThrow("date"))
                val type = cursor.getInt(cursor.getColumnIndexOrThrow("type"))

                if (type == 1 &&
                    body.lowercase().contains("a/c") &&
                    (body.contains("credited") || body.contains("debited"))
                ) {
                    Log.d(
                        tag,
                        "format:  $address, ${
                            SyncUtils.extractTransactionalDetails(
                                date,
                                address,
                                body
                            )
                        }"
                    )
                    SyncUtils.extractTransactionalDetails(
                        date,
                        address,
                        body
                    ).let { transactionList.add(it) }
                }
            } while (cursor.moveToNext())
        }
        cursor?.close()
        return transactionList.reversed()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initialSmsSync(activity: ComponentActivity) {
        val myViewModel = ViewModelProvider(activity).get(AddTransactionViewModel::class.java)
        val isFirstLoad: Boolean =
            SharedPreferencesUtil.getBooleanValueToSharedPreferences(activity, "initial_sync")
        if (isFirstLoad) {
            myViewModel.onEvent(AddTransactionEvent.EnteredTransactionsList(readSMS()))
            myViewModel.onEvent(AddTransactionEvent.SaveAllTransactions)
            SharedPreferencesUtil.setBooleanValueToSharedPreferences(
                activity,
                "initial_sync",
                false
            )
        }
    }
}

