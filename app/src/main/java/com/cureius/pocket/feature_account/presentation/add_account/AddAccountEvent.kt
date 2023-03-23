package com.cureius.pocket.feature_account.presentation.add_account

import com.cureius.pocket.feature_account.domain.model.Bank

sealed class AddAccountEvent {
    data class EnteredHolderName(val value: String) : AddAccountEvent()
    data class SelectedBank(val value: Bank) : AddAccountEvent()
    data class EnteredAccountNumber(val value: String) : AddAccountEvent()
    data class EnteredCardNumber(val value: String) : AddAccountEvent()
    object ToggleAddAccountDialog : AddAccountEvent()
    object SaveAccount : AddAccountEvent()
}