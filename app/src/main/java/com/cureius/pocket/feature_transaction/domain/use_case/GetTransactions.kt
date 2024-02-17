package com.cureius.pocket.feature_transaction.domain.use_case

import com.cureius.pocket.feature_transaction.domain.model.Transaction
import com.cureius.pocket.feature_transaction.domain.repository.TransactionRepository
import com.cureius.pocket.feature_transaction.domain.util.OrderType
import com.cureius.pocket.feature_transaction.domain.util.TransactionOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTransactions(
    private val repository: TransactionRepository
) {
    operator fun invoke(transactionOrder: TransactionOrder = TransactionOrder.Date(OrderType.Descending)): Flow<List<Transaction>> {
        return repository.getTransactions().map { transactions ->
            when (transactionOrder.orderType) {
                is OrderType.Ascending -> {
                    when (transactionOrder) {
                        is TransactionOrder.Title -> transactions.sortedBy { it.type?.lowercase() }
                        is TransactionOrder.Date -> transactions.sortedBy { it.timestamp }
                        is TransactionOrder.Color -> transactions.sortedBy { it.color }
                    }
                }
                is OrderType.Descending -> {
                    when (transactionOrder) {
                        is TransactionOrder.Title -> transactions.sortedByDescending { it.type?.lowercase() }
                        is TransactionOrder.Date -> transactions.sortedByDescending { it.timestamp }
                        is TransactionOrder.Color -> transactions.sortedByDescending { it.color }
                    }
                }
            }
        }
    }
}