package com.cureius.pocket.feature_sms_sync.util

import android.util.Log
import com.cureius.pocket.feature_transaction.domain.model.Transaction
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object SyncUtils {

    private fun dateToTimeStamp(date: String?, time: String?): LocalDate? {

        if (date == null) {
            return null
        }
        val dateTimeString: String = if (time == null) {
            if (date.contains("/")) {
                val newDate = date.replace('/', '-')
                "$newDate 00:00"
            } else {
                "$date 00:00"
            }
        } else {
            if (date.contains("/")) {
                val newDate = date.replace('/', '-')
                "$newDate $time"
            } else {
                "$date $time"
            }
        }
        val dateFormat = SimpleDateFormat("dd-MM-yy HH:mm")
        val dateConv = dateFormat.parse(dateTimeString)

        val formatter = DateTimeFormatter.ofPattern("dd-MM-yy HH:mm")

        // Parse the date and time string into a LocalDateTime object
        val localDateTime = LocalDateTime.parse(dateTimeString, formatter)

        // Extract LocalDate part from LocalDateTime
        return localDateTime.toLocalDate()
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
        return if (regexBody.findAll(text.lowercase()).map { it.value }.toList().isNotEmpty()) {
            regexBody.findAll(text.lowercase()).map { it.value }.toList()[0]
        } else {
            null
        }

    }

    fun extractTransactionalDetails(
        date: Long, address: String, rawMessageBody: String
    ): Transaction? {
        val messageBody = rawMessageBody.lowercase()

        try {
//            println("Extracting transactional details from message body: $date $messageBody")
            // Define the regular expressions for parsing the message body
            val amountRegex =
                Regex("(?i)(amount|txn amount|debited|credited|INR|inr|debited INR|debited inr|credited by Rs |credited by rs |credited for INR|credited for inr|Rs.|rs.|credited for Rs |credited for rs )[\\s:]*[rs.]*(([\\d,]+\\.?\\d*)|([\\d,\\\\.]+))")
            val balanceRegex =
                Regex("(?i)(balance|available balance|Bal INR|bal inr|Available Bal INR|available bal inr|New balance: Rs|new balance: rs\\. |Available Bal Rs |available bal rs |Avl bal:INR |avl bal:inr )[\\s:]*[rs.]*(([\\d,]+\\.\\d{2})|([\\d,\\\\.]+))")
            val transactionUpiIdRegex =
                Regex("(?i)(UPI:|UPI Ref no |UPI Ref ID|upi:|upi ref no |upi ref id )[\\s:]*([0-9]+)")
            val transactionImpsIdRegex =
                Regex("(?i)(transaction id|txn id|reference id|ref|IMPS Ref-|IMPS Ref no|IMPS Ref No\\. |imps ref-|imps ref no|imps ref no\\. )[\\s:]*([0-9]+)")
            val transactionDateRegex2 =
                Regex("(?i)(,|transaction date|txn date|date|on |Dt |dt )[\\s:]*((\\d{2}-\\d{2}-\\d{2})|(\\d{2}-\\d{2}-\\d{4})|(\\d{2}/\\d{2}/\\d{2})|(\\d{2}/\\d{2}/\\d{4}))")
            val transactionDateRegex =
                Regex("(?i)()[\\s:]*((\\d{2}-\\d{2}-\\d{2})|(\\d{2}-\\d{2}-\\d{4})|(\\d{2}/\\d{2}/\\d{2})|(\\d{2}/\\d{2}/\\d{4}))")
            val transactionTimeRegex = Regex("(?i)( )[\\s:]*(\\d{2}:\\d{2})")
            val accountRegex =
                Regex("(?i)(HDFC Bank A/c XX|a/c no XXXXXXXXXX|a/c XXXXXXXX |A/c XX|ac x|a/c xxxxxxxxxxx|AC X)*([0-9]+)")
            val accountRegex0 =
                Regex("(?i)(a/c. xx|a/c no. x|a/c no. xx|a/c no. xxx|a/c no. xxxx|a/c no. xxxxx|a/c no. xxxxxx|a/c no. xxxxxxx|a/c no. xxxxxxxx|a/c no. xxxxxxxxx|a/c x|a/c xx|a/c xxx|a/c xxxx|a/c xxxxx|a/c xxxxxx|a/c xxxxxxx|a/c xxxxxxxx|ac x|ac xx|ac xxx|ac xxxx|ac xxxxx|ac xxxxxx|ac xxxxxxx|ac xxxxxxxx| AC X|A/c XX|A/c xxxxxxxxxxx| ac x|HDFC Bank A/c XX|a/c no XXXXXXXXXX|a/c XXXXXXXX |a/c xxxxxxxxxxx|AC X|a/c xxxxxxxxxxx\\. )[\\s:]*([0-9]+)")

            // Parse the message body using regular expressions
            val amountMatch = amountRegex.find(messageBody)
            val balanceMatch = balanceRegex.find(messageBody)
            val transactionUpiIdMatch = transactionUpiIdRegex.find(messageBody)
            val transactionImpsIdMatch = transactionImpsIdRegex.find(messageBody)
            val transactionDateMatch = transactionDateRegex.find(messageBody)
            val transactionTimeMatch = transactionTimeRegex.find(messageBody)
            val transactionAccountMatch = accountRegex0.find(messageBody)
            val bankName = extractBankNames(address, messageBody) ?: "NAN"


            // Extract the transactional details from the regular expression matches
            val type = if (messageBody.contains("credited") || messageBody.contains("received")) {
                "credited"
            } else {
                "debited"
            }

            val amount = if (amountMatch?.groupValues?.get(2)?.contains(",") == true) {
                amountMatch.groupValues[2].replace(",", "").toDoubleOrNull()
            } else {
                amountMatch?.groupValues?.get(2)?.toDoubleOrNull()

            }
            val balance = if (balanceMatch?.groupValues?.get(2)?.contains(",") == true) {
                balanceMatch.groupValues[2].replace(",", "").toDoubleOrNull()
            } else {
                balanceMatch?.groupValues?.get(2)?.toDoubleOrNull()

            }

            val transactionUpiId = transactionUpiIdMatch?.groupValues?.get(2)
            val transactionImpsId = transactionImpsIdMatch?.groupValues?.get(2)
            var transactionDate = transactionDateMatch?.groupValues?.get(2)
            val transactionTime = transactionTimeMatch?.groupValues?.get(2)
            val transactionAccount = transactionAccountMatch?.groupValues?.get(2)
            // Define input and output formatters
            val inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yy")
            val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val timestamp = date
            val dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneOffset.UTC)
            val formattedDateFromEvent = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            println(formattedDateFromEvent)
            return Transaction(
                type = type,
                account = transactionAccount,
                amount = amount ?: -1.0,
                date = LocalDateTime.ofInstant(Instant.ofEpochMilli(date), ZoneOffset.UTC)
                    .toLocalDate().toEpochDay(),
                event_timestamp = date,
                date_time = formattedDateFromEvent,
                balance = balance,
                UPI_ref = transactionUpiId,
                IMPS_ref = transactionImpsId,
                timestamp = System.currentTimeMillis(),
                bank = bankName,
                body = messageBody.lowercase(),
            )
        } catch (e: Exception) {
            Log.d("tag", messageBody)
            Log.e("tag", "extractTransactionalDetails: ", e)
            return null
        }

    }

}