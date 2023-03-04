package com.cureius.pocket.feature_transaction.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cureius.pocket.feature_transaction.presentation.add_transaction.AddTransactionScreen
import com.cureius.pocket.feature_transaction.presentation.transactions.TransactionsScreen
import com.cureius.pocket.feature_transaction.presentation.util.Screen
import com.cureius.pocket.ui.theme.PocketTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PocketTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colors.background
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
                                    type = NavType.IntType
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
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PocketTheme {
        Greeting("Android")
    }
}