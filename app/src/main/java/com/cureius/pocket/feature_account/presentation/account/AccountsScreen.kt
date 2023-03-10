package com.cureius.pocket.feature_account.presentation.account

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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
import com.cureius.pocket.feature_account.presentation.account.components.*
import kotlinx.coroutines.flow.collectLatest

@Preview
@Composable
fun AccountsScreen(viewModel: AccountsViewModel = hiltViewModel()) {

    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp)
    ) {
        item {
            Text(
                text = "Your Accounts:",
                color = MaterialTheme.colors.onBackground,
                textAlign = TextAlign.Center,
                style = TextStyle(fontWeight = FontWeight.Bold),
                fontSize = 24.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(4.dp, 0.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
        
        
        items(state){ account ->
            AccountItem(
                bankName = account.bank,
                cardNumber = account.card_number!!,
                accountNumber = account.account_number,
                holderName = account.holder_name!!
            )
        }
        item {
            AddAccountCard(onClick = {
                viewModel.onAddClick()
            })
        }
        item { 
            Spacer(modifier = Modifier.height(120.dp))
        }
    }
    if (viewModel.isDialogShown) {
        AddAccountFormDialog(onDismiss = {
            viewModel.onDismissDialog()
        }, onSubmit = {

        }, viewModel)
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddAccountFormDialog(
    onDismiss: () -> Unit,
    onSubmit: () -> Unit,
    viewModel: AccountsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val holderName = viewModel.accountHolderName.value
    val selectedBank = viewModel.bank.value
    val accountNumber = viewModel.accountNumber.value
    val cardNumber = viewModel.cardNumber.value

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val banks = listOf(
        Bank(
            icon = painterResource(id = R.drawable.pnb),
            name = "PNB"
        ),
        Bank(
            icon = painterResource(id = R.drawable.hdfc),
            name = "HDFC"
        ),
        Bank(
            icon = painterResource(id = R.drawable.kotak),
            name = "Kotak"
        ),
        Bank(
            icon = painterResource(id = R.drawable.sbi),
            name = "SBI"
        )
    )
    val color1 = MaterialTheme.colors.background
    val color2 = MaterialTheme.colors.surface
    val mixedColor = Color(
        (color1.red + color2.red) / 2f,
        (color1.green + color2.green) / 2f,
        (color1.blue + color2.blue) / 2f,
    )
    val potShape = RoundedCornerShape(
        topStart = 12.dp,
        topEnd = 12.dp,
        bottomStart = 12.dp,
        bottomEnd = 12.dp
    )

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AccountsViewModel.UiEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                is AccountsViewModel.UiEvent.SaveAccount -> {
                    onDismiss()
                }
            }
        }
    }

    Dialog(
        onDismissRequest = {
            onDismiss()
        },
        properties = DialogProperties(
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
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {

                    item {
                        Text(
                            text = "Add Account",
                            color = MaterialTheme.colors.onBackground,
                            textAlign = TextAlign.Center,
                            style = TextStyle(fontWeight = FontWeight.Bold),
                            fontSize = 20.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(4.dp, 0.dp)
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(14.dp))
                    }
                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            TextField(
                                value = holderName,
                                onValueChange = { newText ->
                                    viewModel.onEvent(AddAccountEvent.EnteredHolderName(newText))
                                },
                                label = { Text(text = "Holder Name") },
                                placeholder = { Text(text = "Enter Account Holders Name") },
                            )
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                    item {
                        Text(
                            text = "Choose Bank",
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
                    }
                    item {
                        LazyRow(
                            modifier = Modifier
                                .height(80.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            items(banks) { item ->
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clickable(
                                            onClick = {
                                                viewModel.onEvent(AddAccountEvent.SelectedBank(item))
                                            }
                                        )
                                        .width(60.dp)
                                        .height(76.dp)
                                        .background(
                                            color = if (selectedBank == item) {
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
                                                painter = item.icon!!,
                                                contentDescription = item.name,
                                                modifier = Modifier
                                                    .size(20.dp),
                                            )

                                        }
                                        Text(
                                            text = item.name!!,
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
                                Spacer(modifier = Modifier.width(6.dp))
                            }
                        }

                    }
                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                    item {
                        Divider()
                    }
                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                    item {
                        Spacer(modifier = Modifier.height(14.dp))
                    }
                    item {
                        Text(
                            text = "Last 3 Digit Of Account Number",
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                            textAlign = TextAlign.End,
                            style = TextStyle(fontWeight = FontWeight.SemiBold),
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .padding(4.dp, 0.dp)
                                .fillMaxWidth()
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                        ) {
                            OutlineTextBox("....XXXXXXXXXXX")
                            Spacer(modifier = Modifier.width(8.dp))
                            SeparatedNumberField(
                                digitText = accountNumber,
                                digitCount = 3,
                                onNumberChange = { value, _ ->
                                    viewModel.onEvent(AddAccountEvent.EnteredAccountNumber(value))
                                },
                            )
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                    item {
                        Text(
                            text = "Last 4 Digit Of Card Number",
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                            textAlign = TextAlign.End,
                            style = TextStyle(fontWeight = FontWeight.SemiBold),
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .padding(4.dp, 0.dp)
                                .fillMaxWidth()
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(10.dp))

                    }
                    item {
                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlineTextBox("....XXXX-XXXX")
                            Spacer(modifier = Modifier.width(8.dp))
                            SeparatedNumberField(
                                digitText = cardNumber,
                                digitCount = 4,
                                onNumberChange = { value, _ ->
                                    viewModel.onEvent(AddAccountEvent.EnteredCardNumber(value))
                                },
                            )
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                    }
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
                                    viewModel.onEvent(
                                        AddAccountEvent.SaveAccount
                                    )
                                    viewModel.onEvent(
                                        AddAccountEvent.EnteredHolderName("")
                                    )
                                    viewModel.onEvent(
                                        AddAccountEvent.EnteredAccountNumber("")
                                    )
                                    viewModel.onEvent(
                                        AddAccountEvent.EnteredCardNumber("")
                                    )
                                    viewModel.onEvent(
                                        AddAccountEvent.SelectedBank(Bank(null, null))
                                    )
                                    onSubmit()
                                },
                                modifier = Modifier.padding(8.dp),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Text(text = "Confirm")
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun DialogPreview() {
    AddAccountFormDialog(onSubmit = {

    }, onDismiss = {

    })
}
