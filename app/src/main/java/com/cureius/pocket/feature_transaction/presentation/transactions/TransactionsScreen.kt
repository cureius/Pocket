package com.cureius.pocket.feature_transaction.presentation.transactions

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cureius.pocket.feature_transaction.presentation.transactions.components.TransactionItem
import com.cureius.pocket.feature_transaction.presentation.util.Screen
import kotlinx.coroutines.launch

@Composable
fun TransactionsScreen(
    navController: NavController, viewModel: TransactionsViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddTransactionScreen.route)
                }, backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Transaction")
            }
        }, scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Your Transactions", style = MaterialTheme.typography.h4
                )
                IconButton(onClick = {
//                    viewModel.onEvent(NotesEvent.ToggleOrderSection)
                    Toast.makeText(navController.context, "Starting sync", Toast.LENGTH_SHORT).show()
                }) {
                    Icon(imageVector = Icons.Default.Download, contentDescription = "Sync")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.transactions) { transaction ->
                    TransactionItem(transaction = transaction, modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(Screen.AddTransactionScreen.route + "?transactionId=${transaction.id}&transactionColor=${transaction.color}")
                        }, onDeleteClick = {
                        viewModel.onEvent(TransactionsEvent.DeleteTransaction(transaction))
                        scope.launch {
                            val result = scaffoldState.snackbarHostState.showSnackbar(
                                message = "Transaction Deleted..!", actionLabel = "Undo"
                            )
                            if (result == SnackbarResult.ActionPerformed) {
                                viewModel.onEvent(TransactionsEvent.RestoreTransaction)
                            }
                        }
                    })
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }

}