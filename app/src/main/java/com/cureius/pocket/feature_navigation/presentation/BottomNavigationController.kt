package com.cureius.pocket.feature_navigation.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cureius.pocket.feature_account.presentation.account.AccountsScreen
import com.cureius.pocket.feature_dashboard.presentation.dashboard.DashboardScreen
import com.cureius.pocket.feature_pot.presentation.pots.PotsScreen

@Composable
fun BottomNavigationController(navController: NavHostController, bottomNavHostController: NavHostController) {
    NavHost(navController = bottomNavHostController, startDestination = "pots") {
            composable("home") {
                DashboardScreen(navController)
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