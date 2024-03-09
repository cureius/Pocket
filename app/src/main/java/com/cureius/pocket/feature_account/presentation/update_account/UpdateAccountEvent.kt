package com.cureius.pocket.feature_account.presentation.update_account

import com.cureius.pocket.feature_account.domain.model.Account

sealed class UpdateAccountEvent {
    data class EnteredHolderName(val value: String) : UpdateAccountEvent()
    data class SelectedBank(val value: String) : UpdateAccountEvent()
    data class EnteredAccountNumber(val value: String) : UpdateAccountEvent()
    data class EnteredCardNumber(val value: String) : UpdateAccountEvent()
    data class SelectAccount(val value: Account) : UpdateAccountEvent()
    object ToggleIsPrimaryAccount : UpdateAccountEvent()
    object ToggleUpdateAccountDialog : UpdateAccountEvent()
    object SaveAccount : UpdateAccountEvent()
}