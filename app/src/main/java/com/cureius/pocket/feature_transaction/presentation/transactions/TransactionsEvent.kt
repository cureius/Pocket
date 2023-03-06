package com.cureius.pocket.feature_transaction.presentation.transactions

import com.cureius.pocket.feature_transaction.domain.model.Transaction
import com.cureius.pocket.feature_transaction.domain.util.TransactionOrder

sealed class TransactionsEvent {
    data class Order(val transactionOrder: TransactionOrder) : TransactionsEvent()
    data class DeleteTransaction(val transaction: Transaction) : TransactionsEvent()
    object RestoreTransaction : TransactionsEvent()
    object ToggleOrderSection : TransactionsEvent()
}