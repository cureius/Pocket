package com.cureius.pocket.feature_upi_payment.presentation.manager



import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import dev.shreyaspatil.easyupipayment.EasyUpiPayment
import dev.shreyaspatil.easyupipayment.listener.PaymentStatusListener
import dev.shreyaspatil.easyupipayment.model.PaymentApp
import dev.shreyaspatil.easyupipayment.model.TransactionDetails
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun UpiPaymentScreen(
    navController: NavHostController,
    upiId: String,
    receiverName: String,
    amount: String,
    description: String,
    viewModel: UpiPaymentViewModel = hiltViewModel()
) {
    val ctx = LocalContext.current
    val activity = (LocalContext.current as? Activity)
    val amount = amount.toDoubleOrNull()
    val upiId = upiId
    val description = description

    println("Amount: $amount")
    println("UPI ID: $upiId")
    println("Name: $receiverName")
    println("Description: $description")


    // on the below line we are creating a column.
    Column(
        // on below line we are adding a modifier to it
        // and setting max size, max height and max width
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
            .fillMaxWidth(),

        // on below line we are adding vertical
        // arrangement and horizontal alignment.
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // on below line we are creating a text
        Text(
            // on below line we are specifying text as
            // Session Management in Android.
            text = "UPI Payments in Android",

            // on below line we are specifying text color.
            color = MaterialTheme.colors.primary ,

            // on below line we are specifying font family
            fontFamily = FontFamily.Default,

            // on below line we are adding font weight
            // and alignment for our text
            fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
        )

        // on below line we are adding spacer
        Spacer(modifier = Modifier.height(5.dp))

        // on below line we are creating a text field for our email.
        TextField(
            // on below line we are specifying
            // value for our email text field.
            value = amount.toString(),

            // on below line we are adding on
            // value change for text field.
            onValueChange = { },

            // on below line we are adding place holder
            // as text as "Enter your email"
            placeholder = { Text(text = "Enter amount to be paid") },

            // on below line we are adding modifier to it
            // and adding padding to it and filling max width
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),

            // on below line we are adding text style
            // specifying color and font size to it.
            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),

            // on below line we are adding
            // single line to it.
            singleLine = true,

            )

        // on below line we are adding spacer
        Spacer(modifier = Modifier.height(5.dp))

        // on below line we are creating a text field for our email.
        if (upiId != null) {
            TextField(
                // on below line we are specifying value
                // for our email text field.
                value = upiId,

                // on below line we are adding on value
                // change for text field.
                onValueChange = { },

                // on below line we are adding place holder as
                // text as "Enter your email"
                placeholder = { Text(text = "Enter UPI Id") },

                // on below line we are adding modifier to it
                // and adding padding to it and filling max width
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),

                // on below line we are adding text style
                // specifying color and font size to it.
                textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),

                // on below line we are adding single line to it.
                singleLine = true,
            )
        }

        // on below line we are adding spacer
        Spacer(modifier = Modifier.height(5.dp))

        // on below line we are creating a text field for our email.
        if (receiverName != null) {
            TextField(
                // on below line we are specifying value
                // for our email text field.
                value = receiverName,

                // on below line we are adding on value
                // change for text field.
                onValueChange = {},

                // on below line we are adding place holder
                // as text as "Enter your email"
                placeholder = { Text(text = "Enter name") },

                // on below line we are adding modifier to it
                // and adding padding to it and filling max width
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),

                // on below line we are adding text style
                // specifying color and font size to it.
                textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),

                // on below line we are adding
                // single line to it.
                singleLine = true,
            )
        }

        // on below line we are adding spacer
        Spacer(modifier = Modifier.height(5.dp))

        // on below line we are creating a text field for our email.
        if (description != null) {
            TextField(
                // on below line we are specifying value
                // for our email text field.
                value = description,

                // on below line we are adding on value change for text field.
                onValueChange = { },

                // on below line we are adding place holder
                // as text as "Enter your email"
                placeholder = { Text(text = "Enter Description") },

                // on below line we are adding modifier to it
                // and adding padding to it and filling max width
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),

                // on below line we are adding text style
                // specifying color and font size to it.
                textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),

                // on below line we are adding single line to it.
                singleLine = true,
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                // on below line we are getting date and then we
                // are setting this date as transaction id.
                val c: Date = Calendar.getInstance().time
                val df = SimpleDateFormat("ddMMyyyyHHmmss", Locale.getDefault())
                val transactionId: String = df.format(c)

                // on below line we are calling make
                // payment method to make payment.
                if (description != null) {
                    if (receiverName != null) {
                        if (upiId != null) {
                            makePayment(
                                amount.toString(),
                                upiId,
                                receiverName,
                                description,
                                transactionId,
                                ctx,
                                activity!!,
                            )
                        }
                    }
                }
            },
            // on below line we are adding modifier to our button.
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // on below line we are adding text for our button
            Text(text = "Make Payment", modifier = Modifier.padding(8.dp))
        }
    }
}

// on below line we are creating
// a make payment method to make payment.
private fun makePayment(
    amount: String,
    upi: String,
    name: String,
    desc: String,
    transactionId: String,
    ctx: Context,
    activity: Activity
) {
    try {
        // START PAYMENT INITIALIZATION
        val easyUpiPayment = EasyUpiPayment(activity) {
            this.paymentApp = PaymentApp.ALL
            this.payeeVpa = upi
            this.payeeName = name
            this.transactionId = transactionId
            this.transactionRefId = transactionId
            this.payeeMerchantCode = transactionId
            this.description = desc
            this.amount = amount
        }
        // END INITIALIZATION

        // Register Listener for Events
        easyUpiPayment.setPaymentStatusListener(object : PaymentStatusListener {
            override fun onTransactionCompleted(transactionDetails: TransactionDetails) {
                // Transaction Completed
                Toast.makeText(ctx, "Transaction Completed", Toast.LENGTH_SHORT).show()
            }

            override fun onTransactionCancelled() {
                // Payment Cancelled by User
                Toast.makeText(ctx, "Transaction Cancelled", Toast.LENGTH_SHORT).show()
            }
        })

        // Start payment / transaction
        easyUpiPayment.startPayment()
    } catch (e: Exception) {
        // on below line we are handling exception.
        e.printStackTrace()
        Toast.makeText(ctx, e.message, Toast.LENGTH_SHORT).show()
    }
}