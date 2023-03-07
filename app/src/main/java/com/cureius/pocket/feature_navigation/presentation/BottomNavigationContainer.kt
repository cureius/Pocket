package com.cureius.pocket.feature_navigation.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cureius.pocket.feature_transaction.presentation.add_transaction.AddTransactionScreen
import com.cureius.pocket.feature_transaction.presentation.transactions.TransactionsScreen
import com.cureius.pocket.feature_transaction.presentation.util.Screen
import com.cureius.pocket.util.components.BottomNavigationBar
import com.cureius.pocket.util.model.BottomNavItem
import com.cureius.pocket.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomNavigationContainer(
    navController: NavHostController,
) {
    // A surface container using the 'background' color from the theme
    val bottomNavController = rememberNavController()
    val shape = RoundedCornerShape(
        topStart = 20.dp,
        topEnd = 20.dp,
        bottomStart = 0.dp,
        bottomEnd = 0.dp
    )
    val home = ImageVector.vectorResource(id = R.drawable.home)
    val record = ImageVector.vectorResource(id = R.drawable.bill)
    val pot = ImageVector.vectorResource(id = R.drawable.pot)
    val account = ImageVector.vectorResource(id = R.drawable.accounts)
    val add = ImageVector.vectorResource(id = R.drawable.add)
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                items = listOf(
                    BottomNavItem(
                        name = "Home",
                        route = "home",
                        icon = home
                    ),
                    BottomNavItem(
                        name = "Records",
                        route = "records",
                        icon = record,
                    ),
                    BottomNavItem(
                        name = "Pots",
                        route = "pots",
                        icon = pot,
                    ),
                    BottomNavItem(
                        name = "Accounts",
                        route = "accounts",
                        icon = account,
                    ),
                ),
                navController = bottomNavController,
                onItemClick = {
                    bottomNavController.navigate(it.route)
                },
                modifier = Modifier
                    .height(60.dp)
                    .background(
                        color = MaterialTheme.colors.surface,
                        shape = shape
                    ),
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController?.navigate("activity")
                }, backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(
                    imageVector = add,
                    contentDescription = "Add Transaction",
                    Modifier.size(56.dp)
                )
            }
        },
    ) {
        BottomNavigationController(navController = navController, bottomNavHostController = bottomNavController)
    }
}


@Composable
fun HomeScreen() {
    Surface(
        color = MaterialTheme.colors.background,
    ) {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = Screen.TransactionsScreen.route
        ) {
            composable(route = Screen.TransactionsScreen.route) {
                TransactionsScreen(navController = navController)
            }
            composable(
                route = Screen.AddTransactionScreen.route + "?transactionId={transactionId}&transactionColor={transactionColor}",
                arguments = listOf(
                    navArgument(name = "transactionId") {
                        type = NavType.LongType
                        defaultValue = -1
                    }, navArgument(name = "transactionColor") {
                        type = NavType.IntType
                        defaultValue = -1
                    })
            ) {
                val color = it.arguments?.getInt("transactionColor") ?: -1
                AddTransactionScreen(
                    navController = navController, transactionColor = color
                )
            }
        }
    }
}


@Composable
fun RecordsScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Records screen")
    }
}
