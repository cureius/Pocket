package com.cureius.pocket.feature_transaction.presentation

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
import com.cureius.pocket.feature_transaction.domain.model.Transaction
import com.cureius.pocket.feature_transaction.presentation.add_transaction.AddTransactionEvent
import com.cureius.pocket.feature_transaction.presentation.add_transaction.AddTransactionScreen
import com.cureius.pocket.feature_transaction.presentation.add_transaction.AddTransactionViewModel
import com.cureius.pocket.feature_transaction.presentation.transactions.TransactionsScreen
import com.cureius.pocket.feature_transaction.presentation.util.Screen
import com.cureius.pocket.ui.theme.PocketTheme
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
            myViewModel.onEvent(AddTransactionEvent.EnteredTransactionsList(readSMS()))
            myViewModel.onEvent(AddTransactionEvent.SaveAllTransactions)
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
                                    type = NavType.IntType
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
                readSMS()
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
        val smsManager = SmsManager.getDefault()
        val messages = mutableListOf<SmsMessage>()

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
                            extractTransactionalDetails(
                                date,
                                address,
                                body
                            ).toString()
                        }"
                    )
                    extractTransactionalDetails(
                        date,
                        address,
                        body
                    )?.let { transactionList.add(it) }
                }
            } while (cursor.moveToNext())
        }

        cursor?.close()
        return transactionList
    }

    private fun dateToTimeStamp(date: String?, time: String?): Long? {

        val dateTimeString: String = if (time == null) {
            if (date?.contains("/") == true) {
                val newDate = date.replace('/', '-')
                "$newDate 00:00"
            } else {
                "$date 00:00"
            }
        } else {
            if (date?.contains("/") == true) {
                val newDate = date.replace('/', '-')
                "$newDate $time"
            } else {
                "$date $time"
            }
        }
        val dateFormat = SimpleDateFormat("dd-MM-yy HH:mm")
        val dateConv = dateFormat.parse(dateTimeString)
        return dateConv?.time
    }

    private fun extractBankNames(address: String, text: String): String? {
        // create an immutable map
        val bankMap = mapOf(
            "HDFCBK" to "HDFC",
            "KOTAKB" to "Kotak",
            "PNBSMS" to "PNB",
            "ICICIB" to "ICICI",
            "SBIINB" to "SBI",
            "AXISBK" to "Axis",
            "SBICRD" to "SBI",
            "AMZNSM" to "Amazon",
            "PAYTMB" to "PayTm",
            "PNBINB" to "PNB",
            "CREDCL" to "Cred"
        )

        bankMap.forEach { (key, value) ->
            if (address.contains(key)) {
                return value
            }
        }
        val regexBody = Regex("(?i)\\b((?:hdfc|pnb|kotak)\\b)")
        return if (regexBody.findAll(text.lowercase())
                .map { it.value }
                .toList().isNotEmpty()
        ) {
            regexBody.findAll(text.lowercase())
                .map { it.value }
                .toList()[0]
        } else {
            null
        }

    }

    private fun extractTransactionalDetails(
        date: Long,
        address: String,
        messageBody: String
    ): Transaction? {
        // Define the regular expressions for parsing the message body
        val amountRegex =
            Regex("(?i)(amount|txn amount|debited|credited|INR|debited INR|credited by Rs |credited for INR|Rs.|credited for Rs )[\\s:]*[rs.]*(([\\d,]+\\.?\\d*)|([\\d,\\\\.]+))")
        val balanceRegex =
            Regex("(?i)(balance|available balance|Bal INR|Available Bal INR|New balance: Rs\\. |Available Bal Rs |Avl bal:INR )[\\s:]*[rs.]*(([\\d,]+\\.\\d{2})|([\\d,\\\\.]+))")
        val transactionUpiIdRegex =
            Regex("(?i)(UPI:|UPI Ref no |UPI Ref ID )[\\s:]*([0-9]+)")
        val transactionImpsIdRegex =
            Regex("(?i)(transaction id|txn id|reference id|ref|IMPS Ref no|IMPS Ref No\\. )[\\s:]*([0-9]+)")
        val transactionDateRegex =
            Regex("(?i)(transaction date|txn date|date|on |Dt )[\\s:]*((\\d{2}-\\d{2}-\\d{2})|(\\d{2}/\\d{2}/\\d{4}))")
        val transactionTimeRegex =
            Regex("(?i)( )[\\s:]*((\\d{2}:\\d{2}))")
        val accountRegex =
            Regex("(?i)(a/c no XXXXXXXXXX|a/c XXXXXXXX)*([0-9]+)")
        // Parse the message body using regular expressions
        val amountMatch = amountRegex.find(messageBody)
        val balanceMatch = balanceRegex.find(messageBody)
        val transactionUpiIdMatch = transactionUpiIdRegex.find(messageBody)
        val transactionImpsIdMatch = transactionImpsIdRegex.find(messageBody)
        val transactionDateMatch = transactionDateRegex.find(messageBody)
        val transactionTimeMatch = transactionTimeRegex.find(messageBody)
        val transactionAccountMatch = accountRegex.find(messageBody)
        val bankName = extractBankNames(address, messageBody) ?: "NAN"

        // Extract the transactional details from the regular expression matches
        val type = if (messageBody.contains("debited")) {
            "debited"
        } else if (messageBody.contains("credited")) {
            "credited"
        } else {
            "NAN"
        }

        val amount = if (amountMatch?.groupValues?.get(2)?.toString()?.contains(",") == true) {
            amountMatch.groupValues[2].replace(",", "").toDoubleOrNull()
        } else {
            amountMatch?.groupValues?.get(2)?.toDoubleOrNull()

        }
        val balance = if (balanceMatch?.groupValues?.get(2)?.toString()?.contains(",") == true) {
            balanceMatch.groupValues[2].replace(",", "").toDoubleOrNull()
        } else {
            balanceMatch?.groupValues?.get(2)?.toDoubleOrNull()

        }

        val transactionUpiId = transactionUpiIdMatch?.groupValues?.get(2)
        val transactionImpsId = transactionImpsIdMatch?.groupValues?.get(2)
        val transactionDate = transactionDateMatch?.groupValues?.get(2)
        val transactionTime = transactionTimeMatch?.groupValues?.get(2)
        val transactionAccount = transactionAccountMatch?.groupValues?.get(2)

        // Create a TransactionalDetails object with the extracted transactional details
        return Transaction(
            id = date,
            type = type,
            account = transactionAccount ?: "NAN",
            amount = amount ?: -1.0,
            date = dateToTimeStamp(transactionDate, transactionTime) ?: -1,
            balance = balance ?: -1.0,
            UPI_ref = transactionUpiId,
            IMPS_ref = transactionImpsId,
            color = Transaction.transactionColors.random().toArgb(),
            timestamp = System.currentTimeMillis(),
            bank = bankName,
            body = messageBody.lowercase(),
            day = "$transactionDate|${transactionTime ?: "00:00"}"
        )
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
