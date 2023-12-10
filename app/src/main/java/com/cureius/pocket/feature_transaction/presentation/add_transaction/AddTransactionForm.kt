package com.cureius.pocket.feature_transaction.presentation.add_transaction

import android.widget.Toast
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.cureius.pocket.R
import com.cureius.pocket.feature_account.domain.model.Bank
import com.cureius.pocket.feature_account.presentation.account.AccountsViewModel
import com.cureius.pocket.feature_account.presentation.add_account.components.OutlineTextBox
import com.cureius.pocket.feature_pot.domain.util.IconDictionary
import com.cureius.pocket.feature_pot.presentation.pots.PotsViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddTransactionForm(
    onDismiss: () -> Unit,
    onSubmit: () -> Unit,
    viewModel: AddTransactionViewModel = hiltViewModel(),
    potsViewModel: PotsViewModel = hiltViewModel(),
    accountsViewModel: AccountsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val transactionAccount = viewModel.transactionAccount.value
    val transactionAmount = viewModel.transactionAmount.value
    val transactionDate = viewModel.transactionDate.value
    val transactionBalance = viewModel.transactionBalance.value
    val transactionType = viewModel.transactionType.value
    val transactionColor = viewModel.transactionColor.value
    val selectedPot = viewModel.pot.value
    val selectedAccount = viewModel.account.value
    var selectedTab by remember { mutableStateOf(0) }
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val mode = remember { mutableStateOf(false) }
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
    val pots = potsViewModel.state.value
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
//    Retrieve Pots Data from db


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
                    onDismiss()
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
                                text = "Add Transaction",
                                color = MaterialTheme.colors.onBackground,
                                textAlign = TextAlign.Center,
                                style = TextStyle(fontWeight = FontWeight.Bold),
                                fontSize = 20.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(4.dp, 0.dp)
                            )
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                        ) {
                            TransactionTypeSwitcher(
                                size = 80.dp,
                                padding = 2.dp,
                                height = 40.dp,
                                width = 150.dp,
                                isSpending = mode.value,
                                parentShape = RoundedCornerShape(50),
                                toggleShape = RoundedCornerShape(50),
                                onClick = { mode.value = !mode.value }
                            )
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                        ) {
                            TextField(
                                value = "",
                                onValueChange = { _ ->
                                    /*viewModel.onEvent(AddTransactionViewModel.EnteredHolderName(newText))*/
                                },
                                label = { Text(text = "What is it!") },
                                placeholder = { Text(text = "Enter Transaction Name") },
                            )
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                    item {
                        Text(
                            text = "Choose Pot",
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
                            modifier = Modifier.height(100.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            items(pots) { item ->
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clickable(onClick = {
                                            viewModel.onEvent(AddTransactionEvent.SelectedPot(item))
                                        })
                                        .width(60.dp)
                                        .height(90.dp)
                                        .background(
                                            color = if (selectedPot == item) {
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
                                                    Color.Green.copy(alpha = 0.1f), CircleShape
                                                )
                                                .padding(8.dp)
                                        ) {
                                            Image(
                                                painter = painterResource(id = IconDictionary.allIcons[item.icon]!!),
                                                contentDescription = item.title!!,
                                                modifier = Modifier.size(40.dp),
                                                colorFilter = null
                                            )
                                        }
                                        Text(
                                            text = item.title!!,
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
                    }
                    item {
                        LazyRow(
                            modifier = Modifier.height(100.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            items(accounts) { item ->
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clickable(onClick = {
//                                            viewModel.onEvent(AddTransactionEvent.SelectedPot(item))
                                        })
                                        .width(60.dp)
                                        .height(90.dp)
                                        .background(
                                            color = if (selectedAccount != null) {
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
                                                    Color.Green.copy(alpha = 0.1f), CircleShape
                                                )
                                                .padding(8.dp)
                                        ) {
                                            print(" item is here " + item.toString())
                                            IconDictionary.allIcons[item.bank]?.let {
                                                painterResource(
                                                    id = it
                                                )
                                            }?.let {
                                                Image(
                                                    painter = it,
                                                    contentDescription = item.bank!!,
                                                    modifier = Modifier.size(40.dp),
                                                    colorFilter = null
                                                )
                                            }
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
                            /*SeparatedNumberField(
                                *//*digitText = accountNumber,*//*
                                digitCount = 3,
                                onNumberChange = { value, _ ->
                                    viewModel.onEvent(AddAccountEvent.EnteredAccountNumber(value))
                                },
                            )*/
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
                            /*SeparatedNumberField(
                                digitText = cardNumber,
                                digitCount = 4,
                                onNumberChange = { value, _ ->
                                    viewModel.onEvent(AddAccountEvent.EnteredCardNumber(value))
                                },
                            )*/
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
                                    /*viewModel.onEvent(
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
                                    )*/
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


@Composable
fun TransactionTypeSwitcher(
    isSpending: Boolean = false,
    size: Dp = 150.dp,
    height: Dp = 50.dp,
    width: Dp = 100.dp,
    iconSize: Dp = size / 3,
    padding: Dp = 10.dp,
    borderWidth: Dp = 1.dp,
    parentShape: Shape = RoundedCornerShape(20),
    toggleShape: Shape = RoundedCornerShape(20),
    animationSpec: AnimationSpec<Dp> = tween(durationMillis = 300),
    onClick: () -> Unit
) {
    val offset by animateDpAsState(
        targetValue = if (isSpending) 0.dp else (width - (padding * 3f)),
        animationSpec = animationSpec, label = ""
    )

    Box(modifier = Modifier
        .width(width * 2)
        .height(height)
        .clip(shape = parentShape)
        .clickable { onClick() }
        .background(MaterialTheme.colors.surface)
    ) {
        Box(
            modifier = Modifier
                .width(width - (padding * 2.8f))
                .height(height)
                .offset(x = offset)
                .padding(all = padding)
                .clip(shape = toggleShape)
                .background(MaterialTheme.colors.primary)
        ) {}
        Row(
            modifier = Modifier
                .border(
                    border = BorderStroke(
                        width = borderWidth,
                        color = MaterialTheme.colors.primary
                    ),
                    shape = parentShape
                )
        ) {
            Box(
                modifier = Modifier
                    .size(width)/*
                    .background(MaterialTheme.colors.primary)*/,
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Spending",
                    style = MaterialTheme.typography.body1,
                    color = if (offset == 0.dp) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface
                )
            }
            Box(
                modifier = Modifier.size(width),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "Income",
                    style = MaterialTheme.typography.body1,
                    color = if (offset == (width - (padding * 3f))) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface
                )
            }
        }
    }
}
