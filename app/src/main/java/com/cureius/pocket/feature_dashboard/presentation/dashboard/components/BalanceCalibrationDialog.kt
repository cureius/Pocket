package com.cureius.pocket.feature_dashboard.presentation.dashboard.components

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.cureius.pocket.R
import com.cureius.pocket.feature_account.domain.model.Bank
import com.cureius.pocket.feature_account.presentation.account.AccountsViewModel
import com.cureius.pocket.feature_transaction.presentation.add_transaction.AddTransactionEvent
import com.cureius.pocket.feature_transaction.presentation.add_transaction.AddTransactionViewModel
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BalanceCalibrationDialog(
    onDismiss: () -> Unit,
    onSubmit: () -> Unit,
    viewModel: AddTransactionViewModel = hiltViewModel(),
    accountsViewModel: AccountsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val transactionType = viewModel.transactionType.value
    val transactionTitle = viewModel.transactionTitle.value
    val transactionAmount = viewModel.transactionAmount.value
    val transactionDate = viewModel.transactionDate.value
    val transactionTime = viewModel.transactionTime.value
    val transactionAccount = viewModel.transactionAccount.value
    val transactionPot = viewModel.transactionPot.value
    val transactionBalance = viewModel.transactionBalance.value
    val transactionColor = viewModel.transactionColor.value
    val selectedPot = viewModel.pot.value
    val selectedAccount = viewModel.account.value
    val scaffoldState = rememberScaffoldState()
    val banks = listOf(
        Bank(
            icon = painterResource(id = R.drawable.pnb), name = "PNB"
        ), Bank(
            icon = painterResource(id = R.drawable.hdfc), name = "HDFC"
        ), Bank(
            icon = painterResource(id = R.drawable.kotak), name = "Kotak"
        ), Bank(
            icon = painterResource(id = R.drawable.sbi), name = "SBI"
        )
    )
    val accounts = accountsViewModel.state.value
    val color1 = MaterialTheme.colors.background
    val color2 = MaterialTheme.colors.surface

    val mixedColor = Color(
        (color1.red + color2.red) / 2f,
        (color1.green + color2.green) / 2f,
        (color1.blue + color2.blue) / 2f,
    )
    val potShape = RoundedCornerShape(
        topStart = 12.dp, topEnd = 12.dp, bottomStart = 12.dp, bottomEnd = 12.dp
    )
    viewModel.onEvent(AddTransactionEvent.EnteredTitle("Account Balance Calibration"))
    viewModel.onEvent(AddTransactionEvent.EnteredType("Recalibration"))
    viewModel.onEvent(AddTransactionEvent.EnteredKind("calibration"))
    viewModel.onEvent(AddTransactionEvent.EnteredDate(LocalDate.now()))
    viewModel.onEvent(AddTransactionEvent.EnteredTime(LocalTime.now()))

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddTransactionViewModel.UiEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                is AddTransactionViewModel.UiEvent.SaveTransaction -> {
                    viewModel.onEvent(AddTransactionEvent.EnteredTitle(""))
                    viewModel.onEvent(AddTransactionEvent.EnteredAmount(""))
                    viewModel.onEvent(AddTransactionEvent.EnteredDate(LocalDate.now()))
                    viewModel.onEvent(AddTransactionEvent.EnteredTime(LocalTime.now()))
                    viewModel.onEvent(AddTransactionEvent.SelectedPot(null))
                    viewModel.onEvent(AddTransactionEvent.SelectedAccount(null))
                }
            }
        }
    }

    Dialog(
        onDismissRequest = {
            onDismiss()
        }, properties = DialogProperties(
            usePlatformDefaultWidth = true
        )
    ) {
        Card(
            elevation = 8.dp,
            shape = RoundedCornerShape(15.dp),
        ) {
            Box(
                modifier = Modifier
                    .background(mixedColor)
                    .padding(16.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    item {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Account Balance",
                                color = MaterialTheme.colors.onBackground,
                                textAlign = TextAlign.Center,
                                style = TextStyle(fontWeight = FontWeight.Bold),
                                fontSize = 20.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(4.dp, 0.dp)
                            )
                        }
                    } // Add Transaction Header
                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                    } // Spacer 20
                    if (accounts.isNotEmpty()) {
                        item {
                            Text(
                                text = "Choose Account",
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                                textAlign = TextAlign.Start,
                                style = TextStyle(fontWeight = FontWeight.SemiBold),
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .padding(4.dp, 0.dp)
                                    .fillMaxWidth()
                            )
                        } // Choose Accounts
                        item {
                            LazyRow(
                                modifier = Modifier.height(100.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                items(accounts) { item ->
                                    var bank = banks.find {
                                        it.name == item.bank;
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .clickable(onClick = {
                                                viewModel.onEvent(
                                                    AddTransactionEvent.SelectedAccount(
                                                        item
                                                    )
                                                )
                                            })
                                            .width(60.dp)
                                            .height(90.dp)
                                            .background(
                                                color = if (selectedAccount == item) {
                                                    MaterialTheme.colors.primary.copy(alpha = 0.5f)
                                                } else {
                                                    MaterialTheme.colors.surface
                                                }, potShape
                                            ),
                                    ) {
                                        Column(
                                            modifier = Modifier.fillMaxHeight(),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Bottom
                                        ) {
                                            Box(
                                                contentAlignment = Alignment.BottomCenter,
                                                modifier = Modifier
                                                    .background(
                                                        Color.Green.copy(alpha = 0.1f),
                                                        CircleShape
                                                    )
                                                    .padding(8.dp)
                                            ) {
                                                Image(
                                                    painter = bank!!.icon!!,
                                                    contentDescription = bank.name,
                                                    modifier = Modifier.size(20.dp),
                                                )

                                            }
                                            item.card_number?.let {
                                                Text(
                                                    text = it,
                                                    color = MaterialTheme.colors.onSurface,
                                                    textAlign = TextAlign.Center,
                                                    style = TextStyle(fontWeight = FontWeight.Bold),
                                                    fontSize = 12.sp,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(4.dp, 8.dp)
                                                )
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                }
                            }
                        } // Row Of Selectable Pre-added Accounts
                        item {
                            Spacer(modifier = Modifier.height(10.dp))
                        } // Spacer 20
                    }
                    if (selectedAccount != null) {
                        item {
                            Spacer(modifier = Modifier.height(20.dp))
                        } // Spacer 20
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                TextField(
                                    value = transactionBalance?: "",
                                    onValueChange = { amount ->

                                        // Ensure that the input is numeric
                                        try {
                                            viewModel.onEvent(
                                                AddTransactionEvent.EnteredBalance(
                                                    amount.toDouble()
                                                )
                                            )
                                        } catch (e: NumberFormatException) {
                                            Toast.makeText(
                                                context,
                                                "Invalid Amount",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    },
                                    label = { Text(text = "Account balance") },
                                    placeholder = { Text(text = "Enter Account Balance") },
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Number
                                    )
                                )
                            }
                        } // Transaction Amount Text Field
                    }
                    item {
                        Divider()
                    } // Divider
                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                    } // Spacer 20
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            // Rounded Button
                            OutlinedButton(
                                onClick = { onDismiss() },
                                border = BorderStroke(1.dp, MaterialTheme.colors.onBackground),
                                modifier = Modifier.padding(8.dp),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Text(text = "Cancel")
                            }
                            Button(
                                onClick = {
                                    println("Selected Account: $selectedAccount")
                                    println("Account Balance: $transactionBalance")
                                    viewModel.onEvent(
                                        AddTransactionEvent.SaveTransaction
                                    )
                                    viewModel.onEvent(AddTransactionEvent.EnteredTitle(""))
                                    viewModel.onEvent(AddTransactionEvent.EnteredAmount(""))
                                    viewModel.onEvent(AddTransactionEvent.EnteredDate(LocalDate.now()))
                                    viewModel.onEvent(AddTransactionEvent.EnteredTime(LocalTime.now()))
                                    viewModel.onEvent(AddTransactionEvent.SelectedPot(null))
                                    viewModel.onEvent(AddTransactionEvent.SelectedAccount(null))
                                    onSubmit()
                                },
                                modifier = Modifier.padding(8.dp),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Text(text = "Confirm")
                            }
                        }
                    } // Cancel And Submit Buttons
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun BalanceCalibrationDialogPreview() {
    BalanceCalibrationDialog(onSubmit = {

    }, onDismiss = {

    })
}
