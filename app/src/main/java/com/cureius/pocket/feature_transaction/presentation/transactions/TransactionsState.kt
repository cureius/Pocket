package com.cureius.pocket.feature_transaction.presentation.transactions

import com.cureius.pocket.feature_transaction.domain.model.Transaction
import com.cureius.pocket.feature_transaction.domain.util.OrderType
import com.cureius.pocket.feature_transaction.domain.util.TransactionOrder

data class TransactionsState(
    val transactions: List<Transaction> = emptyList(),
    val transactionOrder: TransactionOrder = TransactionOrder.Date(OrderType.Descending),
    val isOrderSelectionVisible: Boolean = false
)