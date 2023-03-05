package com.cureius.pocket.feature_transaction.domain.use_case

data class TransactionUseCases(
    val getTransaction: GetTransaction,
    val getTransactions: GetTransactions,
    val addTransaction: AddTransaction,
    val addTransactions: AddTransactions,
    val deleteTransaction: DeleteTransaction
)