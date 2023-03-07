package com.cureius.pocket.feature_navigation.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cureius.pocket.feature_account.presentation.account.AccountsScreen
import com.cureius.pocket.feature_dashboard.presentation.dashboard.DashboardScreen
import com.cureius.pocket.feature_pot.presentation.pots.PotsScreen

@Composable
fun BottomNavigationController(navController: NavHostController) {
        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                DashboardScreen()
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