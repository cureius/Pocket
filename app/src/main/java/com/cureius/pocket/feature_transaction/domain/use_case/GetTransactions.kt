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
        return repository.
        getTransactions().map { transactions ->
            when (transactionOrder.orderType) {
                is OrderType.Ascending -> {
                    when (transactionOrder) {
                        is TransactionOrder.Amount -> transactions.sortedBy { it.amount}
                        is TransactionOrder.Date -> transactions.sortedBy { it.event_timestamp }
                        is TransactionOrder.Type -> transactions.sortedBy { it.type?.lowercase()}
                    }
                }
                is OrderType.Descending -> {
                    when (transactionOrder) {
                        is TransactionOrder.Amount -> transactions.sortedByDescending { it.amount }
                        is TransactionOrder.Date -> transactions.sortedByDescending { it.event_timestamp }
                        is TransactionOrder.Type -> transactions.sortedByDescending { it.type?.lowercase() }
                    }
                }
            }
        }
    }
}