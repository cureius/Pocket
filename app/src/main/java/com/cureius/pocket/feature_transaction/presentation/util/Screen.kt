package com.cureius.pocket.feature_transaction.presentation.util

sealed class Screen(val route: String) {
    object TransactionsScreen : Screen("transactions_screen")
    object AddTransactionScreen : Screen("add_transaction_screen")
    object AddPotScreen : Screen("add_pot_screen")
    object AddAccountScreen : Screen("add_account_screen")
    object AddTransactionForm : Screen("add_transaction_form")
    object AddPotForm : Screen("add_pot_form")
    object AddAccountForm : Screen("add_account_form")
    object TransactionsForm : Screen("transactions_form")
    object PotsScreen : Screen("pots_screen")
    object AccountsScreen : Screen("accounts_screen")
    object DashboardScreen : Screen("dashboard_screen")
    object QrScannerScreen : Screen("qr_scanner_screen")
    object UpiPaymentScreen : Screen("upi_payment_screen")
}