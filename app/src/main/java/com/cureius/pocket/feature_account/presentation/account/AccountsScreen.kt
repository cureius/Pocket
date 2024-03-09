package com.cureius.pocket.feature_account.presentation.account

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cureius.pocket.feature_account.presentation.account.components.AccountItem
import com.cureius.pocket.feature_account.presentation.account.components.AddAccountCard
import com.cureius.pocket.feature_account.presentation.add_account.AddAccountEvent
import com.cureius.pocket.feature_account.presentation.add_account.AddAccountFormDialog
import com.cureius.pocket.feature_account.presentation.add_account.AddAccountViewModel
import com.cureius.pocket.feature_account.presentation.update_account.UpdateAccountEvent
import com.cureius.pocket.feature_account.presentation.update_account.UpdateAccountFormDialog
import com.cureius.pocket.feature_account.presentation.update_account.UpdateAccountViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Preview
@Composable
fun AccountsScreen(
    viewModel: AccountsViewModel = hiltViewModel(),
    addAccountViewModel: AddAccountViewModel = hiltViewModel(),
    updateAccountViewModel: UpdateAccountViewModel = hiltViewModel(),
) {

    val state = viewModel.state.value
    val balance = viewModel.totalBalance.value
    Log.d("Accounts ", "AccountsScreen: $balance")
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    Scaffold {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp)
        ) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(0.dp, 0.dp)
                ) {
                    Text(
                        text = "Accounts",
                        color = MaterialTheme.colors.onBackground,
                        textAlign = TextAlign.Center,
                        style = TextStyle(fontWeight = FontWeight.Bold),
                        fontSize = 24.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(4.dp, 0.dp)
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
            items(state) { account ->
                AccountItem(
                    bankName = account.bank,
                    cardNumber = account.card_number!!,
                    accountNumber = account.account_number,
                    holderName = account.holder_name!!
                ) {
                    updateAccountViewModel.onEvent(
                        UpdateAccountEvent.EnteredAccountNumber(
                            account.account_number
                        )
                    )
                    updateAccountViewModel.onEvent(UpdateAccountEvent.EnteredCardNumber(account.card_number))
                    updateAccountViewModel.onEvent(UpdateAccountEvent.EnteredHolderName(account.holder_name))
                    updateAccountViewModel.onEvent(UpdateAccountEvent.SelectedBank(account.bank))
                    updateAccountViewModel.onEvent(UpdateAccountEvent.SelectAccount(account))
                    updateAccountViewModel.onEvent(UpdateAccountEvent.ToggleUpdateAccountDialog)
                }
            }
            if (state.isEmpty()) {
                item {
                    AddAccountCard(onClick = {
                        addAccountViewModel.onEvent(AddAccountEvent.ToggleAddAccountDialog)
                    })
                }
            }

            item {
                Spacer(modifier = Modifier.height(140.dp))
            }
        }
        if (addAccountViewModel.dialogVisibility.value) {
            AddAccountFormDialog(onDismiss = {
                addAccountViewModel.onEvent(AddAccountEvent.ToggleAddAccountDialog)
            }, onSubmit = {

            }, isOneAccountAlreadyPresent = viewModel.state.value.isNotEmpty())
        }
        if (updateAccountViewModel.dialogVisibility.value) {
            UpdateAccountFormDialog(onDismiss = {
                updateAccountViewModel.onEvent(UpdateAccountEvent.ToggleUpdateAccountDialog)
            }, onSubmit = {

            }, isOneAccountAlreadyPresent = false)
        }
    }

}
