package com.cureius.pocket.feature_transaction.presentation.add_transaction

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cureius.pocket.feature_transaction.domain.model.Transaction
import com.cureius.pocket.feature_transaction.presentation.add_transaction.components.TransparentHintTextField
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@Composable
fun AddTransactionScreen(
    navController: NavController,
    transactionColor: Int,
    viewModel: AddTransactionViewModel = hiltViewModel()
) {

    val typeState = viewModel.transactionType.value
    val accountState = viewModel.transactionAccount.value
    val amountState = viewModel.transactionAmount.value
    val dateState = viewModel.transactionDate.value
    val balanceState = viewModel.transactionBalance.value
    val scaffoldState = rememberScaffoldState()

    val backgroundAnimation = remember {
        Animatable(
            Color(if (transactionColor != -1) transactionColor else viewModel.transactionColor.value)
        )
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddTransactionViewModel.UiEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is AddTransactionViewModel.UiEvent.SaveTransaction -> {
                    navController.navigateUp()
                }
            }
        }
    }

//    Scaffold(
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = {
//                    viewModel.onEvent(AddTransactionEvent.SaveTransaction)
//                }, backgroundColor = MaterialTheme.colors.primary
//            ) {
//                Icon(imageVector = Icons.Default.Save, contentDescription = "Save Transaction")
//            }
//        }, scaffoldState = scaffoldState
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(backgroundAnimation.value)
//                .padding(16.dp)
//        ) {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(8.dp),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Transaction.transactionColors.forEach { color ->
//                    val colorInt = color.toArgb()
//                    Box(modifier = Modifier
//                        .size(50.dp)
//                        .shadow(15.dp, CircleShape)
//                        .clip(CircleShape)
//                        .background(color)
//                        .border(
//                            width = 3.dp,
//                            color = if (viewModel.transactionColor.value == colorInt) {
//                                Color.Black
//                            } else Color.Transparent,
//                            shape = CircleShape
//                        )
//                        .clickable {
//                            scope.launch {
//                                backgroundAnimation.animateTo(
//                                    targetValue = Color(colorInt), animationSpec = tween(
//                                        durationMillis = 500
//                                    )
//                                )
//                            }
//                            viewModel.onEvent(AddTransactionEvent.ChangeColor(colorInt))
//                        })
//                }
//            }
//            Spacer(modifier = Modifier.height(16.dp))
//            TransparentHintTextField(
//                text = typeState.text,
//                hint = typeState.hint,
//                onValueChange = {
//                    viewModel.onEvent(AddTransactionEvent.EnteredType(it))
//                },
//                onFocusChange = {
//                    viewModel.onEvent(AddTransactionEvent.ChangeTypeFocus(it))
//                },
//                isHintVisible = typeState.isHintVisible,
//                singleLine = true,
//                textStyle = MaterialTheme.typography.h5
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//            TransparentHintTextField(
//                text = accountState.text,
//                hint = accountState.hint,
//                onValueChange = {
//                    viewModel.onEvent(AddTransactionEvent.EnteredAccount(it))
//                },
//                onFocusChange = {
//                    viewModel.onEvent(AddTransactionEvent.ChangeAccountFocus(it))
//                },
//                isHintVisible = accountState.isHintVisible,
//                textStyle = MaterialTheme.typography.body1,
//                singleLine = true,
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//            TransparentHintTextField(
//                text = amountState.text,
//                hint = amountState.hint,
//                onValueChange = {
//                    viewModel.onEvent(AddTransactionEvent.EnteredAmount(it.toDouble()))
//                },
//                onFocusChange = {
//                    viewModel.onEvent(AddTransactionEvent.ChangeAmountFocus(it))
//                },
//                isHintVisible = amountState.isHintVisible,
//                textStyle = MaterialTheme.typography.body1,
//                singleLine = true
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//            TransparentHintTextField(
//                text = dateState.text,
//                hint = dateState.hint,
//                onValueChange = {
//                    viewModel.onEvent(AddTransactionEvent.EnteredDate(it.toLong()))
//                },
//                onFocusChange = {
//                    viewModel.onEvent(AddTransactionEvent.ChangeDateFocus(it))
//                },
//                isHintVisible = dateState.isHintVisible,
//                textStyle = MaterialTheme.typography.body1,
//                singleLine = true
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//            TransparentHintTextField(
//                text = balanceState.text,
//                hint = balanceState.hint,
//                onValueChange = {
//                    viewModel.onEvent(AddTransactionEvent.EnteredBalance(it.toDouble()))
//                },
//                onFocusChange = {
//                    viewModel.onEvent(AddTransactionEvent.ChangeBalanceFocus(it))
//                },
//                isHintVisible = balanceState.isHintVisible,
//                textStyle = MaterialTheme.typography.body1,
//                singleLine = true
//            )
//
//        }
//    }


}