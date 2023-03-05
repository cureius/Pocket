package com.cureius.pocket.feature_transaction.domain.use_case

import com.cureius.pocket.feature_transaction.domain.model.InvalidTransactionException
import com.cureius.pocket.feature_transaction.domain.model.Transaction
import com.cureius.pocket.feature_transaction.domain.repository.TransactionRepository

class AddTransactions(
    private val repository: TransactionRepository
) {
    @Throws(InvalidTransactionException::class)
    suspend operator fun invoke(transactions: List<Transaction>) {
        repository.insertTransactions(transactions)
    }

}