package com.cureius.pocket.feature_transaction.presentation.add_transaction

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
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
import com.cureius.pocket.feature_pot.domain.util.IconDictionary
import com.cureius.pocket.feature_pot.presentation.pots.PotsViewModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
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
    var selectedTab by remember { mutableStateOf(0) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    var pickedDate by remember {
        mutableStateOf(LocalDate.now())
    }
    var pickedTime by remember {
        mutableStateOf(LocalTime.now())
    }
    val formattedDate by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("dd MMM yyyy")
                .format(pickedDate)
        }
    }
    val formattedTime by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("hh:mm a")
                .format(pickedTime)
        }
    }

    val dateDialogState = rememberMaterialDialogState()
    val timeDialogState = rememberMaterialDialogState()

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val mode = remember { mutableStateOf(true) }
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
    val pots = potsViewModel.templatePots.value
    val accounts = accountsViewModel.state.value
    val color1 = MaterialTheme.colors.background
    val color2 = MaterialTheme.colors.surface
    val calendar = ImageVector.vectorResource(id = R.drawable.edit_calendar)
    val clock = ImageVector.vectorResource(id = R.drawable.clock)

    val mixedColor = Color(
        (color1.red + color2.red) / 2f,
        (color1.green + color2.green) / 2f,
        (color1.blue + color2.blue) / 2f,
    )
    val potShape = RoundedCornerShape(
        topStart = 12.dp, topEnd = 12.dp, bottomStart = 12.dp, bottomEnd = 12.dp
    )

    val selectedDates = remember { mutableStateOf<List<LocalDate>>(listOf()) }
    val disabledDates = listOf(
        LocalDate.now().minusDays(7),
        LocalDate.now().minusDays(12),
        LocalDate.now().plusDays(3),
    )


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
                Column {
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
                        } // Add Transaction Header
                        item {
                            Spacer(modifier = Modifier.height(20.dp))
                        } // Spacer 20
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(40.dp)
                            ) {
                                viewModel.onEvent(AddTransactionEvent.EnteredType(if (mode.value) "Spending" else "Income"))
                                TransactionTypeSwitcher(size = 80.dp,
                                    padding = 2.dp,
                                    height = 40.dp,
                                    width = 150.dp,
                                    isSpending = mode.value,
                                    parentShape = RoundedCornerShape(50),
                                    toggleShape = RoundedCornerShape(50),
                                    onClick = {
                                        mode.value = !mode.value
                                        viewModel.onEvent(AddTransactionEvent.EnteredType(if (mode.value) "Spending" else "Income"))
                                    })
                            }
                        } // Spending Income Toggle
                        item {
                            Spacer(modifier = Modifier.height(20.dp))
                        } // Spacer 20
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                TextField(
                                    value = transactionTitle,
                                    onValueChange = { transactionName ->
                                        viewModel.onEvent(
                                            AddTransactionEvent.EnteredTitle(
                                                transactionName
                                            )
                                        )
                                    },
                                    label = { Text(text = "What is it!") },
                                    placeholder = { Text(text = "Enter Transaction Name") },
                                )
                            }
                        } // Transaction Name Text Field
                        item {
                            Spacer(modifier = Modifier.height(20.dp))
                        } // Spacer 20
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                TextField(
                                    value = transactionAmount,
                                    onValueChange = { amount ->
                                        viewModel.onEvent(AddTransactionEvent.EnteredAmount(amount))
                                    },
                                    label = { Text(text = "How much?") },
                                    placeholder = { Text(text = "Enter Transaction Amount") },
                                )
                            }
                        } // Transaction Amount Text Field
                        item {
                            Spacer(modifier = Modifier.height(20.dp))
                        } // Spacer 20
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(2.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .weight(1f)
                                        .border(
                                            1.dp,
                                            MaterialTheme.colors.primaryVariant,
                                            RoundedCornerShape(8.dp)
                                        )
                                        .padding(horizontal = 2.dp)
                                        .clickable {
//                                        showDatePicker = true
                                            dateDialogState.show()
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(

                                        ) {
                                            Text(
                                                text = "Date",
                                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                                                textAlign = TextAlign.Start,
                                                style = TextStyle(fontWeight = FontWeight.SemiBold),
                                                fontSize = 8.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                modifier = Modifier
                                                    .padding(4.dp, 0.dp)
                                            )
                                            Text(
                                                text = if (formattedDate.toString() == "") "Select Date" else formattedDate.toString(),
                                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.9f),
                                                textAlign = TextAlign.Start,
                                                style = TextStyle(fontWeight = FontWeight.SemiBold),
                                                fontSize = 16.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                modifier = Modifier
                                                    .padding(4.dp, 0.dp)
                                            )
                                        }
                                        IconButton(onClick = {
                                        }) {
                                            Icon(
                                                imageVector = calendar,
                                                contentDescription = "Select date",
                                                tint = MaterialTheme.colors.onSurface.copy(alpha = 0.4f),
                                            )
                                        }
                                    }
                                }
                                Spacer(
                                    modifier =
                                    Modifier.width(8.dp)
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .weight(1f)
                                        .border(
                                            1.dp,
                                            MaterialTheme.colors.primaryVariant,
                                            RoundedCornerShape(8.dp)
                                        )
                                        .padding(horizontal = 2.dp)
                                        .clickable {
//                                        showTimePicker = true
                                            timeDialogState.show()
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(

                                        ) {
                                            Text(
                                                text = "Time",
                                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                                                textAlign = TextAlign.Start,
                                                style = TextStyle(fontWeight = FontWeight.SemiBold),
                                                fontSize = 8.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                modifier = Modifier
                                                    .padding(4.dp, 0.dp)
                                            )
                                            Text(
                                                text = if (formattedTime.toString() == "") "-- --" else formattedTime.toString(),
                                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.9f),
                                                textAlign = TextAlign.Start,
                                                style = TextStyle(fontWeight = FontWeight.SemiBold),
                                                fontSize = 16.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                modifier = Modifier
                                                    .padding(4.dp, 0.dp)
                                            )
                                        }
                                        IconButton(onClick = {
                                        }) {
                                            Icon(
                                                imageVector = clock,
                                                contentDescription = "Select time",
                                                tint = MaterialTheme.colors.onSurface.copy(alpha = 0.4f),
                                            )
                                        }
                                    }
                                }
                            }

                        } // Transaction Date Text Field
                        item {
                            Spacer(modifier = Modifier.height(20.dp))
                        } // Spacer 20
                    }
                    if (accounts.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
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
                    }
                    if (pots.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
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
                            } // Choose Pots Header
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
                                                    viewModel.onEvent(
                                                        AddTransactionEvent.SelectedPot(
                                                            item
                                                        )
                                                    )
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
                                                    modifier = Modifier.padding(8.dp)
                                                ) {
                                                    if(IconDictionary.allIcons[item.icon] != null ){
                                                        Image(
                                                            painter = painterResource(id = IconDictionary.allIcons[item.icon]!!),
                                                            contentDescription = item.title,
                                                            modifier = Modifier.size(40.dp),
                                                            colorFilter = if (selectedPot == item) {
                                                                ColorFilter.tint(MaterialTheme.colors.surface)
                                                            } else {
                                                                ColorFilter.tint(
                                                                    MaterialTheme.colors.primary.copy(
                                                                        alpha = 0.5f
                                                                    )
                                                                )
                                                            }
                                                        )
                                                    }
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

                            } // Row Of Selectable Pre-added Pots
                            item {
                                Spacer(modifier = Modifier.height(10.dp))
                            } // Spacer 20
                        }
                    }
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
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
                                        viewModel.onEvent(
                                            AddTransactionEvent.SaveTransaction
                                        )
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

    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(text = "Ok")
            negativeButton(text = "Cancel")
        }
    ) {
        datepicker(
            initialDate = LocalDate.now(),
            title = "Pick a date",
        ) {
            pickedDate = it
            viewModel.onEvent(AddTransactionEvent.EnteredDate(it.toEpochDay()))
        }
    }

    MaterialDialog(
        dialogState = timeDialogState,
        buttons = {
            positiveButton(text = "Ok")
            negativeButton(text = "Cancel")
        }
    ) {
        timepicker(
            initialTime = LocalTime.now(),
            title = "Pick a time",
        ) {
            pickedTime = it
            viewModel.onEvent(AddTransactionEvent.EnteredTime(it.toNanoOfDay()))
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
        animationSpec = animationSpec,
        label = ""
    )

    Box(modifier = Modifier
        .width(width * 2)
        .height(height)
        .clip(shape = parentShape)
        .clickable { onClick() }
        .background(MaterialTheme.colors.surface)) {
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
            modifier = Modifier.border(
                border = BorderStroke(
                    width = borderWidth, color = MaterialTheme.colors.primary
                ), shape = parentShape
            )
        ) {
            Box(
                modifier = Modifier.size(width)/*
                    .background(MaterialTheme.colors.primary)*/, contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Spending",
                    style = MaterialTheme.typography.body1,
                    color = if (offset == 0.dp) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface
                )
            }
            Box(
                modifier = Modifier.size(width), contentAlignment = Alignment.Center
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
