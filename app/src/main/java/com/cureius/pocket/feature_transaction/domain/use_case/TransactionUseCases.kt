package com.cureius.pocket.feature_transaction.domain.use_case

data class TransactionUseCases(
    val getTransaction: GetTransaction,
    val getTransactionByEventTimestamp: GetTransactionByEventTimestamp,
    val getTransactionsCreatedOnCurrentMonth: GetTransactionsCreatedOnCurrentMonth,
    val getTransactionsOfAccount: GetTransactionsOfAccount,
    val getLatestTransactionWithBalanceForAccount: GetLatestTransactionWithBalanceForAccount,
    val getTransactionsForDateRange: GetTransactionsForDateRange,
    val getTransactions: GetTransactions,
    val addTransaction: AddTransaction,
    val updateTransaction: UpdateTransaction,
    val addTransactions: AddTransactions,
    val deleteTransaction: DeleteTransaction
)