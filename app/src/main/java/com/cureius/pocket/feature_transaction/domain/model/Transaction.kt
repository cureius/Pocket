package com.cureius.pocket.feature_transaction.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cureius.pocket.ui.theme.BabyBlue
import com.cureius.pocket.ui.theme.LightGreen
import com.cureius.pocket.ui.theme.RedOrange
import com.cureius.pocket.ui.theme.RedPink
import com.cureius.pocket.ui.theme.Violet

@Entity
data class Transaction(
    /*-------------From-SMS---------------*/
    val title: String? = null,
    val type: String,
    val account: String,
    val amount: Double,
    val date: Long,
    val balance: Double,
    val day: String? = null,
    val amount_string: String? = null,
    val UPI_ref: String? = null,
    val IMPS_ref: String? = null,
    val card_number: String? = null,
    val place: String? = null,
    val bank: String? = null,
    val body: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    /*----------For-Flow-Tracking---------*/
    val color: Int,
    val kind: String? = null,
    val pot: String? = null,
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
