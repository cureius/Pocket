package com.cureius.pocket.feature_transaction.domain.use_case

import com.cureius.pocket.feature_transaction.domain.model.Transaction
import com.cureius.pocket.feature_transaction.domain.repository.TransactionRepository

class GetTransactionByEventTimestamp(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(eventTimestamp: Long): Transaction? {
        return repository.getTransactionByEventTimestamp(eventTimestamp)
    }
}