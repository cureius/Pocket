package com.cureius.pocket.feature_transaction.presentation.transactions

import android.Manifest
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cureius.pocket.feature_transaction.presentation.add_transaction.AddTransactionViewModel
import com.cureius.pocket.feature_transaction.presentation.transactions.components.OrderSection
import com.cureius.pocket.feature_transaction.presentation.transactions.components.TransactionItem
import com.cureius.pocket.feature_transaction.presentation.util.Screen
import com.cureius.pocket.feature_transaction.presentation.util.components.CameraPermissionTextProvider
import com.cureius.pocket.feature_transaction.presentation.util.components.PermissionDialog
import com.cureius.pocket.feature_transaction.presentation.util.components.PhoneCallPermissionTextProvider
import com.cureius.pocket.feature_transaction.presentation.util.components.RecordAudioPermissionTextProvider
import kotlinx.coroutines.launch

@Composable
fun TransactionsScreen(
    navController: NavController?, viewModel: TransactionsViewModel? = hiltViewModel(), addViewModel: AddTransactionViewModel = hiltViewModel()
) {
    val state = viewModel?.state?.value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val dialogQueue = viewModel?.visiblePermissionDialogQueue

    val permissionsToRequest = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.CALL_PHONE,
    )

    val cameraPermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            viewModel?.onPermissionResult(
                permission = Manifest.permission.READ_SMS,
                isGranted = isGranted
            )
        }
    )

    val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { perms ->
            permissionsToRequest.forEach { permission ->
                viewModel?.onPermissionResult(
                    permission = permission,
                    isGranted = perms[permission] == true
                )
            }
        }
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController?.navigate(Screen.AddTransactionScreen.route)
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
                    viewModel?.onEvent(TransactionsEvent.ToggleOrderSection)
                }) {
                    Icon(imageVector = Icons.Default.Sort, contentDescription = "Sort")
                }
            }
            if (state != null) {
                AnimatedVisibility(
                    visible = state.isOrderSelectionVisible,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    OrderSection(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                        transactionOrder = state.transactionOrder,
                        onOrderChange = {
                            viewModel.onEvent(TransactionsEvent.Order(it))
                        })
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                if (state != null) {
                    items(state.transactions) { transaction ->
                        TransactionItem(transaction = transaction, modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController?.navigate(Screen.AddTransactionScreen.route + "?transactionId=${transaction.id}&transactionColor=${transaction.color}")
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

        dialogQueue?.reversed()?.forEach { permission ->
            PermissionDialog(
                permissionTextProvider = when (permission) {
                    Manifest.permission.CAMERA -> {
                        CameraPermissionTextProvider()
                    }
                    Manifest.permission.RECORD_AUDIO -> {
                        RecordAudioPermissionTextProvider()
                    }
                    Manifest.permission.CALL_PHONE -> {
                        PhoneCallPermissionTextProvider()
                    }
                    else -> return@forEach
                },
                isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                    LocalContext.current as Activity,
                    permission
                ),
                onDismiss = viewModel::dismissDialog,
                onOkClick = {
                    viewModel.dismissDialog()
                    multiplePermissionResultLauncher.launch(
                        arrayOf(permission)
                    )
                },
                onGoToAppSettingsClick = { }
            )
        }
    }

}


@Preview(showSystemUi = true)
@Composable
fun TransactionsScreenPreview() {
    TransactionsScreen(
        navController = rememberNavController(),
        viewModel = null
    )
}