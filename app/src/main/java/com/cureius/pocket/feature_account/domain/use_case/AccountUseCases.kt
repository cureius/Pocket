package com.cureius.pocket.feature_account.domain.use_case

data class AccountUseCases(
    val getAccount: GetAccount,
    val getAccounts: GetAccounts,
    val addAccount: AddAccount,
    val updateAccount: UpdateAccount,
    val deleteAccount: DeleteAccount,
)



