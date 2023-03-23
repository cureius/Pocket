package com.cureius.pocket.feature_account.presentation.account

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cureius.pocket.feature_account.domain.model.Account
import com.cureius.pocket.feature_account.domain.use_case.AccountUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val accountUseCases: AccountUseCases, savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(listOf<Account>())
    val state: State<List<Account>> = _state
    private var getAccountsJob: Job? = null

    init {
        getAccounts()
    }

    private fun getAccounts() {
        getAccountsJob?.cancel()
        getAccountsJob = accountUseCases.getAccounts().onEach { accounts ->
            _state.value = accounts
        }.launchIn(viewModelScope)
    }
}