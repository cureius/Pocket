package com.cureius.pocket.feature_transaction.presentation.util

sealed class Screen(val route: String) {
    object TransactionsScreen : Screen("transactions_screen")
    object AddTransactionScreen : Screen("add_transaction_screen")
}