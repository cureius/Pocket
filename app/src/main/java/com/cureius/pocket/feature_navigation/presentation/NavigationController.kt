package com.cureius.pocket.feature_navigation.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.cureius.pocket.feature_account.presentation.account.AccountsScreen
import com.cureius.pocket.feature_pot.presentation.configure_pots.ConfigurePotsScreen
import com.cureius.pocket.feature_pot.presentation.pots.PotsScreen
import com.cureius.pocket.feature_qr_scanner.presentation.widgets.QrScanner
import com.cureius.pocket.feature_upi_payment.presentation.manager.UpiPaymentScreen

@Composable
fun NavigationController(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = "root",
        startDestination = "bottom_nav"
    ) {
        bottomNavGraph(navController= navController)
        composable("bottom_nav") {
            BottomNavigationContainer(navController)
        }
        composable("activity") {
            RecordsScreen()
        }
        composable("wallet") {
            PotsScreen(navController)
        }
        composable("profile") {
            AccountsScreen()
        }
        composable("configure_pots") {
            ConfigurePotsScreen()
        }
        composable("qr_scanner") {
            QrScanner(navController)
        }
        composable("upi_payment") {
            UpiPaymentScreen()
        }
    }
}


fun NavGraphBuilder.bottomNavGraph(navController: NavHostController) {
    navigation(
        route = "bottom_navigation",
        startDestination = "home"
    ) {

        composable("home") {
            BottomNavigationContainer(navController)
        }
        composable("records") {
            RecordsScreen()
        }
        composable("pots") {
            PotsScreen(navController)
        }
        composable("accounts") {
            AccountsScreen()
        }
    }
}