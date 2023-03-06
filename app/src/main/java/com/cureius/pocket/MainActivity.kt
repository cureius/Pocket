package com.cureius.pocket

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cureius.pocket.feature_sms_sync.util.SyncUtils
import com.cureius.pocket.feature_transaction.domain.model.Transaction
import com.cureius.pocket.feature_transaction.presentation.add_transaction.AddTransactionEvent
import com.cureius.pocket.feature_transaction.presentation.add_transaction.AddTransactionScreen
import com.cureius.pocket.feature_transaction.presentation.add_transaction.AddTransactionViewModel
import com.cureius.pocket.feature_transaction.presentation.transactions.TransactionsScreen
import com.cureius.pocket.feature_transaction.presentation.util.Screen
import com.cureius.pocket.ui.theme.PocketTheme
import com.cureius.pocket.util.SharedPreferencesUtil
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.math.log

@AndroidEntryPoint
class MainActivity : ComponentActivity(){
    private val TAG: String = "MainActivity"
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
            val myViewModel = ViewModelProvider(this).get(AddTransactionViewModel::class.java)
            val isFirstLoad: Boolean = SharedPreferencesUtil.getBooleanValueToSharedPreferences(this, "initial_sync")
            if(isFirstLoad){
                myViewModel.onEvent(AddTransactionEvent.EnteredTransactionsList(readSMS()))
                myViewModel.onEvent(AddTransactionEvent.SaveAllTransactions)
                SharedPreferencesUtil.setBooleanValueToSharedPreferences(this, "initial_sync", false)
            }
        }
        setContent {
            PocketTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.TransactionsScreen.route
                    ) {
                        composable(route = Screen.TransactionsScreen.route) {
                            TransactionsScreen(navController = navController)
                        }
                        composable(
                            route = Screen.AddTransactionScreen.route + "?transactionId={transactionId}&transactionColor={transactionColor}",
                            arguments = listOf(
                                navArgument(name = "transactionId") {
                                    type = NavType.LongType
                                    defaultValue = -1
                                }, navArgument(name = "transactionColor") {
                                    type = NavType.IntType
                                    defaultValue = -1
                                })
                        ) {
                            val color = it.arguments?.getInt("transactionColor") ?: -1
                            AddTransactionScreen(
                                navController = navController, transactionColor = color
                            )
                        }
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val myViewModel = ViewModelProvider(this).get(AddTransactionViewModel::class.java)
                val isFirstLoad: Boolean = SharedPreferencesUtil.getBooleanValueToSharedPreferences(this, "initial_sync")
                if(isFirstLoad){
                    myViewModel.onEvent(AddTransactionEvent.EnteredTransactionsList(readSMS()))
                    myViewModel.onEvent(AddTransactionEvent.SaveAllTransactions)
                    SharedPreferencesUtil.setBooleanValueToSharedPreferences(this, "initial_sync", false)
                }
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
        var transactionList = mutableListOf<Transaction>()
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
                        TAG,
                        "format:  $address, ${
                            SyncUtils.extractTransactionalDetails(
                                date,
                                address,
                                body
                            ).toString()
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

}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PocketTheme {
        Greeting("Android")
    }
}
