package com.cureius.pocket.feature_navigation.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cureius.pocket.feature_account.presentation.account.AccountsScreen
import com.cureius.pocket.feature_dashboard.presentation.dashboard.DashboardScreen
import com.cureius.pocket.feature_pot.presentation.pots.PotsScreen
import com.cureius.pocket.feature_qr_scanner.presentation.widgets.QrScanner

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BottomNavigationController(navController: NavHostController, bottomNavHostController: NavHostController) {
    NavHost(navController = bottomNavHostController, startDestination = "home") {
            composable("home") {
                DashboardScreen(navController)
            }
            composable("qr_scanner") {
                QrScanner()
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