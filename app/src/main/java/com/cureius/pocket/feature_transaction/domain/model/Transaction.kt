package com.cureius.pocket.feature_transaction.domain.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.cureius.pocket.ui.theme.BabyBlue
import com.cureius.pocket.ui.theme.LightGreen
import com.cureius.pocket.ui.theme.RedOrange
import com.cureius.pocket.ui.theme.RedPink
import com.cureius.pocket.ui.theme.Violet

@Entity(
    indices = [Index(value = ["event_timestamp"], unique = true)]
)
data class Transaction(
    /*-------------From-SMS---------------*/
    val title: String? = null,
    val type: String? = null,
    val account: String? = null,
    val amount: Double? = null,
    val date: Long? = null,
    val event_timestamp: Long? = null,
    val date_time: String? = date?.let { java.time.LocalDate.ofEpochDay(it).toString() },
    val balance: Double? = null,
    val day: String? = date?.let { java.time.LocalDate.ofEpochDay(it).dayOfWeek.toString() },
    val day_of_month: String? = date?.let { java.time.LocalDate.ofEpochDay(it).dayOfMonth.toString() },
    val amount_string: String? = amount?.let { it.toString() },
    val UPI_ref: String? = null,
    val IMPS_ref: String? = null,
    val card_number: String? = null,
    val place: String? = null,
    val bank: String? = null,
    val body: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    /*----------For-Flow-Tracking---------*/
    val color: Int? = null,
    val kind: String? = null,
    val pot: String? = null,
    val pot_id: Long? = null,
    val expense_type: String? = null,
    /*--------------For-DB----------------*/
    val timestamp: Long,
    @PrimaryKey val id: Long? = null

) {
    companion object {
        val transactionType = listOf("debited", "credited")
        val potOf = listOf("wallet", "EMIs", "savings", "investments")
        val kindOf = listOf("asset", "liability")
        val expenseType =
            listOf("food", "entertainment", "travel", "home", "subscription", "self_care", "cash")
        val transactionColors = listOf(RedOrange, LightGreen, Violet, BabyBlue, RedPink)
    }
}

class InvalidTransactionException(message: String) : Exception(message)
