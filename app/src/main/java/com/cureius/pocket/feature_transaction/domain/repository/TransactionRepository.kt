package com.cureius.pocket.feature_transaction.domain.repository

import com.cureius.pocket.feature_transaction.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getTransactions(): Flow<List<Transaction>>

    suspend fun getTransactionById(id: Int): Transaction?

    suspend fun insertTransaction(transaction: Transaction)

    suspend fun insertTransactions(transactions: List<Transaction>)

    suspend fun deleteTransaction(transaction: Transaction)
}