package com.cureius.pocket.feature_upi_payment.presentation.manager



import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.cureius.pocket.MainActivity
import dev.shreyaspatil.easyupipayment.EasyUpiPayment
import dev.shreyaspatil.easyupipayment.listener.PaymentStatusListener
import dev.shreyaspatil.easyupipayment.model.PaymentApp
import dev.shreyaspatil.easyupipayment.model.TransactionDetails
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun UpiPaymentScreen() {
    val ctx = LocalContext.current
    val activity = (LocalContext.current as? Activity)

    val amount = remember {
        mutableStateOf(TextFieldValue())
    }
    val upiId = remember {
        mutableStateOf(TextFieldValue())
    }
    val name = remember {
        mutableStateOf(TextFieldValue())
    }
    val description = remember {
        mutableStateOf(TextFieldValue())
    }


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
            value = amount.value,

            // on below line we are adding on
            // value change for text field.
            onValueChange = { amount.value = it },

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
        TextField(
            // on below line we are specifying value
            // for our email text field.
            value = upiId.value,

            // on below line we are adding on value
            // change for text field.
            onValueChange = { upiId.value = it },

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

        // on below line we are adding spacer
        Spacer(modifier = Modifier.height(5.dp))

        // on below line we are creating a text field for our email.
        TextField(
            // on below line we are specifying value
            // for our email text field.
            value = name.value,

            // on below line we are adding on value
            // change for text field.
            onValueChange = { name.value = it },

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

        // on below line we are adding spacer
        Spacer(modifier = Modifier.height(5.dp))

        // on below line we are creating a text field for our email.
        TextField(
            // on below line we are specifying value
            // for our email text field.
            value = description.value,

            // on below line we are adding on value change for text field.
            onValueChange = { description.value = it },

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

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                // on below line we are getting date and then we
                // are setting this date as transaction id.
                val c: Date = Calendar.getInstance().getTime()
                val df = SimpleDateFormat("ddMMyyyyHHmmss", Locale.getDefault())
                val transcId: String = df.format(c)

                // on below line we are calling make
                // payment method to make payment.
                makePayment(
                    amount.value.text,
                    upiId.value.text,
                    name.value.text,
                    description.value.text,
                    transcId,
                    ctx,
                    activity!!,
                )
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
    transcId: String, ctx: Context, activity: Activity
) {
    try {
        // START PAYMENT INITIALIZATION
        val easyUpiPayment = EasyUpiPayment(activity) {
            this.paymentApp = PaymentApp.ALL
            this.payeeVpa = upi
            this.payeeName = name
            this.transactionId = transcId
            this.transactionRefId = transcId
            this.payeeMerchantCode = transcId
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

class UpiPaymentStatusListener : PaymentStatusListener {
    override fun onTransactionCancelled() {
        TODO("Not yet implemented")
    }

    override fun onTransactionCompleted(transactionDetails: TransactionDetails) {
        TODO("Not yet implemented")
    }

}