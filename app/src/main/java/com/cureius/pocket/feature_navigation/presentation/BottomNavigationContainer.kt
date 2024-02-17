package com.cureius.pocket.feature_navigation.presentation

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cureius.pocket.R
import com.cureius.pocket.feature_account.presentation.add_account.AddAccountEvent
import com.cureius.pocket.feature_account.presentation.add_account.AddAccountFormDialog
import com.cureius.pocket.feature_account.presentation.add_account.AddAccountViewModel
import com.cureius.pocket.feature_pot.presentation.add_pot.AddPotEvent
import com.cureius.pocket.feature_pot.presentation.add_pot.AddPotViewModel
import com.cureius.pocket.feature_pot.presentation.add_pot.cmponents.AddPotDialog
import com.cureius.pocket.feature_transaction.presentation.add_transaction.AddTransactionEvent
import com.cureius.pocket.feature_transaction.presentation.add_transaction.AddTransactionForm
import com.cureius.pocket.feature_transaction.presentation.add_transaction.AddTransactionScreen
import com.cureius.pocket.feature_transaction.presentation.add_transaction.AddTransactionViewModel
import com.cureius.pocket.feature_transaction.presentation.transactions.TransactionsScreen
import com.cureius.pocket.feature_transaction.presentation.util.Screen
import com.cureius.pocket.util.components.BottomNavigationBar
import com.cureius.pocket.util.model.BottomNavItem

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomNavigationContainer(
    navController: NavHostController,
    addPotViewModel: AddPotViewModel = hiltViewModel(),
    addAccountViewModel: AddAccountViewModel = hiltViewModel(),
    addTransactionViewModel: AddTransactionViewModel = hiltViewModel()
) {
    // A surface container using the 'background' color from the theme
    val bottomNavController = rememberNavController()
    val shape = RoundedCornerShape(
        topStart = 20.dp, topEnd = 20.dp, bottomStart = 0.dp, bottomEnd = 0.dp
    )
    val home = ImageVector.vectorResource(id = R.drawable.home)
    val record = ImageVector.vectorResource(id = R.drawable.bill)
    val pot = ImageVector.vectorResource(id = R.drawable.pot)
    val account = ImageVector.vectorResource(id = R.drawable.accounts)
    val add = ImageVector.vectorResource(id = R.drawable.add)

    val context = LocalContext.current
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                items = listOf(
                    BottomNavItem(
                        name = "Home", route = "home", icon = home
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
                        color = MaterialTheme.colors.surface, shape = shape
                    ),
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    when (bottomNavController.currentDestination?.route) {
                        "home" -> {
                            Toast.makeText(
                                context, "Dash Board Screen !", Toast.LENGTH_SHORT
                            ).show()
                        }

                        "records" -> {
                            addTransactionViewModel.onEvent(AddTransactionEvent.ToggleAddTransactionDialog)
                        }

                        "pots" -> {
                            addPotViewModel.onEvent(AddPotEvent.ToggleAddAccountDialog)
                        }

                        "accounts" -> {
                            addAccountViewModel.onEvent(AddAccountEvent.ToggleAddAccountDialog)
                        }

                        else -> {
                            Toast.makeText(
                                context, "Unknown Screen !", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }, backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(
                    imageVector = add, contentDescription = "Add Transaction", Modifier.size(56.dp)
                )
            }
        },
    ) {
        BottomNavigationController(
            navController = navController, bottomNavHostController = bottomNavController
        )
    }

    if (addAccountViewModel.dialogVisibility.value) {
        AddAccountFormDialog(onDismiss = {
            addAccountViewModel.onEvent(AddAccountEvent.ToggleAddAccountDialog)
        }, onSubmit = {

        })
    }
    if (addPotViewModel.dialogVisibility.value) {
        AddPotDialog(onDismiss = {
            addPotViewModel.onEvent(AddPotEvent.ToggleAddAccountDialog)
        }, onSubmit = {

        })
    }
    if (addTransactionViewModel.dialogVisibility.value ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AddTransactionForm(onDismiss = {
                addTransactionViewModel.onEvent(AddTransactionEvent.ToggleAddTransactionDialog)
            }, onSubmit = {
                addTransactionViewModel.onEvent(AddTransactionEvent.ToggleAddTransactionDialog)
            })
        }
    }
}

@Composable
fun RecordsScreen() {
    Surface(
        color = MaterialTheme.colors.background,
    ) {
        val navController = rememberNavController()
        NavHost(
            navController = navController, startDestination = Screen.TransactionsScreen.route
        ) {
            composable(route = Screen.TransactionsScreen.route) {
                TransactionsScreen(navController = navController)
            }
            composable(
                route = Screen.AddTransactionScreen.route + "?transactionId={transactionId}&transactionColor={transactionColor}",
                arguments = listOf(navArgument(name = "transactionId") {
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
