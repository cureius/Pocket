package com.cureius.pocket.feature_transaction.presentation.transactions

import com.cureius.pocket.feature_transaction.domain.model.Transaction

data class TransactionsState(
    val transactions: List<Transaction> = emptyList(),
)