package com.cureius.pocket.feature_account.presentation.add_account

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cureius.pocket.feature_account.domain.model.Account
import com.cureius.pocket.feature_account.domain.model.Bank
import com.cureius.pocket.feature_account.domain.use_case.AccountUseCases
import com.cureius.pocket.feature_transaction.domain.model.InvalidTransactionException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddAccountViewModel @Inject constructor(
    private val accountUseCases: AccountUseCases,
) : ViewModel() {

    private val _dialogVisibility = mutableStateOf(false)
    val dialogVisibility: State<Boolean> = _dialogVisibility

    private val _accountHolderName = mutableStateOf("")
    val accountHolderName: State<String> = _accountHolderName

    private val _accountNumber = mutableStateOf("")
    val accountNumber: State<String> = _accountNumber

    private val _cardNumber = mutableStateOf("")
    val cardNumber: State<String> = _cardNumber

    private val _isPrimaryAccount = mutableStateOf(false)
    val isPrimaryAccount: State<Boolean> = _isPrimaryAccount

    private val _bank = mutableStateOf(
        Bank(
            null, null
        )
    )
    val bank: State<Bank> = _bank

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    private var currentAccountId: Int? = null

    sealed class UiEvent {
        data class ShowSnackBar(val message: String) : UiEvent()
        object SaveAccount : UiEvent()
    }

    init {
        _accountHolderName.value = ""
        _accountNumber.value = ""
        _cardNumber.value = ""
        _isPrimaryAccount.value = false
    }

    fun onEvent(event: AddAccountEvent) {
        when (event) {
            is AddAccountEvent.ToggleAddAccountDialog -> {
                _dialogVisibility.value = !_dialogVisibility.value
            }

            is AddAccountEvent.ToggleIsPrimaryAccount -> {
                _isPrimaryAccount.value = !_isPrimaryAccount.value
            }

            is AddAccountEvent.EnteredHolderName -> {
                _accountHolderName.value = event.value
            }

            is AddAccountEvent.SelectedBank -> {
                _bank.value = event.value
            }

            is AddAccountEvent.EnteredAccountNumber -> {
                _accountNumber.value = event.value
            }

            is AddAccountEvent.EnteredCardNumber -> {
                _cardNumber.value = event.value
            }

            is AddAccountEvent.SaveAccount -> {
                viewModelScope.launch {
                    try {
                        accountUseCases.addAccount(
                            Account(
                                accountNumber.value,
                                bank.value.name.toString(),
                                accountHolderName.value,
                                cardNumber.value,
                                is_primary = isPrimaryAccount.value,
                                timestamp = System.currentTimeMillis(),
                                id = currentAccountId
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveAccount)
                    } catch (e: InvalidTransactionException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackBar(
                                message = e.message ?: "Cant Save Account"
                            )
                        )
                    }
                }
            }
        }
    }
}