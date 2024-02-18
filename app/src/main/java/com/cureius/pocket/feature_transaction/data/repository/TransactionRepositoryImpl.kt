package com.cureius.pocket.feature_transaction.data.repository

import com.cureius.pocket.feature_transaction.data.data_source.TransactionDao
import com.cureius.pocket.feature_transaction.domain.model.Transaction
import com.cureius.pocket.feature_transaction.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow

class TransactionRepositoryImpl(
    private val dao: TransactionDao
) : TransactionRepository {
    override fun getTransactions(): Flow<List<Transaction>> {
        return dao.getTransactions();
    }

    override fun getTransactionsCreatedOnCurrentMonth(): Flow<List<Transaction>> {
        return dao.getTransactionsCreatedOnCurrentMonth();
    }

    override suspend fun getTransactionById(id: Long): Transaction? {
        return dao.getTransactionById(id)
    }

    override fun getTransactionsForDateRange(
        start: Long,
        end: Long
    ): Flow<List<Transaction>> {
        return dao.getTransactionsForDateRange(start, end)
    }

    override suspend fun insertTransaction(transaction: Transaction) {
        return dao.insertTransaction(transaction)
    }

    override suspend fun insertTransactions(transactions: List<Transaction>) {
        return dao.insertTransactions(transactions)
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        return dao.deleteTransaction(transaction)
    }

}