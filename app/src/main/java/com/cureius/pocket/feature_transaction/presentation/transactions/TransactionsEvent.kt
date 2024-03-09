package com.cureius.pocket.feature_transaction.presentation.transactions

import com.cureius.pocket.feature_pot.domain.model.Pot
import com.cureius.pocket.feature_transaction.domain.model.Transaction
import com.cureius.pocket.feature_transaction.domain.util.TransactionOrder

sealed class TransactionsEvent {
    data class Order(val transactionOrder: TransactionOrder) : TransactionsEvent()
    data class SelectPots(val pot: Pot?) : TransactionsEvent()
    data class RemovePots(val pot: Pot?) : TransactionsEvent()
    data class DeleteTransaction(val transaction: Transaction) : TransactionsEvent()
    object RestoreTransaction : TransactionsEvent()
    object ToggleOrderSection : TransactionsEvent()
    object TogglePotsSection : TransactionsEvent()
    data class MonthSelected(val value: String?) : TransactionsEvent()
    data class SearchTransactionText(val value: String?) : TransactionsEvent()
    object ToggleMonthPickerDialog : TransactionsEvent()
}