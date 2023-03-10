package com.cureius.pocket.feature_account.domain.use_case

import com.cureius.pocket.feature_account.domain.model.Account
import com.cureius.pocket.feature_account.domain.repository.AccountRepository

class DeleteAccount(
    private val repository: AccountRepository
) {
    suspend operator fun invoke(account: Account) {
        repository.deleteAccount(account)
    }
}