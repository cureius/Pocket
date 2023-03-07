package com.cureius.pocket.feature_navigation.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.cureius.pocket.feature_account.presentation.account.AccountsScreen
import com.cureius.pocket.feature_dashboard.presentation.dashboard.DashboardScreen
import com.cureius.pocket.feature_pot.presentation.pots.PotsScreen

@Composable
fun NavigationController(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = "root",
        startDestination = "bottom_navigation"
    ) {
        bottomNavGraph(navController= navController)
        composable("bottom_nav") {
            BottomNavigationContainer(navController)
        }
        composable("activity") {
            RecordsScreen()
        }
        composable("wallet") {
            PotsScreen()
        }
        composable("profile") {
            AccountsScreen()
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
            PotsScreen()
        }
        composable("accounts") {
            AccountsScreen()
        }
    }
}