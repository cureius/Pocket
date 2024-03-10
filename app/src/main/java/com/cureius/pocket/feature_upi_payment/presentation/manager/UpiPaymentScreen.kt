package com.cureius.pocket.feature_upi_payment.presentation.manager



import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

    val context = LocalContext.current
    val paymentResult = remember { mutableStateOf<String?>(null) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val resultCode = result.resultCode

        if (resultCode == android.app.Activity.RESULT_OK ) {
            // Payment successful
            paymentResult.value = "Payment successful"
        } else {
            // Payment failed or was canceled
            paymentResult.value = "Payment failed or canceled"
        }
    }


    val activity = (LocalContext.current as? Activity)
    val amount = amount.toDoubleOrNull()
    val upiId = upiId
    val description = description
    amount?.let { UpiPaymentEvent.SetPaymentAmount(it) }?.let { viewModel.onEvent(it) }
    upiId.let { UpiPaymentEvent.SetPaymentReceiverId(it) }.let { viewModel.onEvent(it) }
    receiverName.let { UpiPaymentEvent.SetPaymentReceiverName(it) }.let { viewModel.onEvent(it) }
    description.let { UpiPaymentEvent.SetPaymentDescription(it) }.let { viewModel.onEvent(it) }


    println("Amount: $amount")
    println("UPI ID: $upiId")
    println("Name: $receiverName")
    println("Description: $description")
    println("ViewModel: ${viewModel.state.value}")


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
            value = viewModel.amount.value.toString(),

            // on below line we are adding on
            // value change for text field.
            onValueChange = {
                viewModel.onEvent(UpiPaymentEvent.SetPaymentAmount(it.toDoubleOrNull() ?: 0.0))
            },

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
            viewModel.receiverId.value?.let {id ->
                TextField(
                    // on below line we are specifying value
                    // for our email text field.
                    value = id,

                    // on below line we are adding on value
                    // change for text field.
                    onValueChange = {
                        viewModel.onEvent(UpiPaymentEvent.SetPaymentReceiverId(it))
                    },

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
        }

        // on below line we are adding spacer
        Spacer(modifier = Modifier.height(5.dp))

        // on below line we are creating a text field for our email.
        if (receiverName != null) {
            TextField(
                // on below line we are specifying value
                // for our email text field.
                value = viewModel.receiverName.value.toString(),

                // on below line we are adding on value
                // change for text field.
                onValueChange = {
                    viewModel.onEvent(UpiPaymentEvent.SetPaymentReceiverName(it))
                },

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
                value = viewModel.description.value.toString(),

                // on below line we are adding on value change for text field.
                onValueChange = {
                    viewModel.onEvent(UpiPaymentEvent.SetPaymentDescription(it))
                },

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

                initiatePayment(context, launcher)
                // on below line we are calling make
                // payment method to make payment.
                try {
//                    if (description != null) {
//                        if (receiverName != null) {
//                            if (upiId != null) {
//                                makePayment(
//                                    viewModel.amount.value.toString(),
//                                    viewModel.receiverId.value.toString(),
//                                    viewModel.receiverName.value.toString(),
//                                    viewModel.description.value.toString(),
//                                    transactionId,
//                                    ctx,
//                                    activity!!,
//                                )
//                            }
//                        }
//                    }
                } catch (e: Exception) {
                    e.printStackTrace()
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

private fun initiatePayment(context: android.content.Context, launcher: androidx.activity.result.ActivityResultLauncher<android.content.Intent>) {
    val upiUri = Uri.Builder()
        .scheme("upi")
        .authority("pay")
        .appendQueryParameter("pa", "palsouraj@ybl@upi") // Replace with your UPI ID
        .appendQueryParameter("pn", "Souraj Pal")    // Replace with payee name
        .appendQueryParameter("mc", "")              // Replace with merchant code if any
        .appendQueryParameter("tid", "4256363562356q4142345")            // Replace with transaction ID
        .appendQueryParameter("tr", "transactionReference") // Replace with a unique transaction reference
        .appendQueryParameter("tn", "Transaction Note")     // Replace with transaction note
        .appendQueryParameter("am", "1.00")        // Replace with transaction amount
        .appendQueryParameter("cu", "INR")          // Replace with currency code
        .build()

    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = upiUri

    val chooser = Intent.createChooser(intent, "Pay")
    if (chooser.resolveActivity(context.packageManager) != null) {
        launcher.launch(chooser)
    } else {
        // Handle the case where no UPI app is available
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
            this.description = "Testing amount"
            this.amount = "1.0"
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