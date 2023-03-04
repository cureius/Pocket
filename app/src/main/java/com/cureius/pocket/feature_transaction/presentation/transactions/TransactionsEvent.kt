package com.cureius.pocket.feature_transaction.presentation.transactions

import com.cureius.pocket.feature_transaction.domain.model.Transaction

sealed class TransactionsEvent {
    data class DeleteTransaction(val transaction: Transaction) : TransactionsEvent()
    object RestoreTransaction : TransactionsEvent()
}