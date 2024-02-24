package com.cureius.pocket.feature_transaction.domain.use_case

import com.cureius.pocket.feature_transaction.domain.model.Transaction
import com.cureius.pocket.feature_transaction.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow

class GetLatestTransactionWithBalanceForAccount(
    private val repository: TransactionRepository
) {
    operator fun invoke(accountNumber: String): Flow<Transaction>? {
        return repository.getLatestTransactionWithBalanceForAccountNumber(accountNumber)
    }
}